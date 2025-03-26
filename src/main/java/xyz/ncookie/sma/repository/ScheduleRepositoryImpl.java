package xyz.ncookie.sma.repository;

import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.entity.Schedule;
import xyz.ncookie.sma.entity.User;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    private final String QUERY_SCHEDULE_WRITER_JOIN_STRING =
            "SELECT s.*, u.id as user_id, u.name, u.email FROM schedule s INNER JOIN user u ON s.user_id = u.id";

    public ScheduleRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 일정 저장
     * @param schedule entity 객체
     * @return 전달받은 entity 객체에 추가적인 값 입력 후 반환
     */
    @Override
    public Schedule saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");

        // 현재 시각
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        // DB에 삽입할 파라미터 설정
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", schedule.getUser().getId());
        parameters.put("task", schedule.getTask());
        parameters.put("password", schedule.getPassword());
        parameters.put("created_at", now);
        parameters.put("modified_at", now);

        // DB 저장 및 ID 조회
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        // entity 값 설정
        schedule.setId(key.longValue());
        schedule.setCreatedAt(now.toLocalDateTime());
        schedule.setModifiedAt(now.toLocalDateTime());

        return schedule;
    }

    /**
     * 일정 단일 조회
     * @param scheduleId 일정 ID
     * @return Optional<Schedule>
     */
    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        List<Schedule> result = jdbcTemplate.query(
                QUERY_SCHEDULE_WRITER_JOIN_STRING + " WHERE s.id = ?",
                scheduleRowMapper(),
                scheduleId
        );

        return result.stream().findAny();
    }

    /**
     * 일정 
     * @param pageable size, page 등의 정보를 담고 있음
     * @param modifiedDate 수정일로 검색
     * @param userId 유저 id 기준으로 검색
     * @return 지정된 조건에 해당하는 데이터들을 Page 객체에 담아 반환 (수정일 기준 내림차순 정렬)
     */
    @Override
    public Page<ScheduleResponseDto> findAll(Pageable pageable, String modifiedDate, Long userId) {
        String rowCountQuery = "SELECT COUNT(*) FROM schedule";     // 데이터 개수 카운트하는 쿼리
        StringBuilder query = new StringBuilder(QUERY_SCHEDULE_WRITER_JOIN_STRING + " WHERE 1=1");  // 조건에 맞는 데이터 조회하는 쿼리
        List<Object> params = new ArrayList<>();    // prepared statement 쿼리에 매개변수로 전달할 리스트

        if (!modifiedDate.isBlank()) {
            query.append(" AND DATE_FORMAT(s.modified_at, '%Y-%m-%d') = ?");
            params.add(modifiedDate);
        }

        if (userId != -1) {
            query.append(" AND user_id = ?");
            params.add(userId);
        }

        query.append(" ORDER BY s.modified_at DESC");

        query.append(" LIMIT ?");
        params.add(pageable.getPageSize());

        query.append(" OFFSET ?");
        params.add(pageable.getOffset());

        // 쿼리 실행
        Integer totalCount = jdbcTemplate.queryForObject(rowCountQuery, Integer.class);
        List<ScheduleResponseDto> schedules = jdbcTemplate.query(query.toString(), scheduleRowMapper(), params.toArray()).stream()
                .map(ScheduleResponseDto::from)
                .toList();

        // queryForObject 실행 결과가 아무것도 없으면 null 값을 반환하기 때문에, NPE가 발생할 수 있다고 IDE가 경고해서 예외처리 한다.
        // 사실 COUNT(*)의 쿼리 결과는 항상 0 이상이기 때문에 별 의미는 없긴 함
        return new PageImpl<>(schedules, pageable, totalCount == null ? 0 : totalCount);
    }

    @Override
    public Optional<Schedule> updateSchedule(Long scheduleId, Long userId, String task) {
        int updatedRow = jdbcTemplate.update(
                "UPDATE schedule SET task = ?, modified_at = NOW() WHERE id = ? AND user_id = ?",
                task,
                scheduleId,
                userId
        );

        return updatedRow == 0
                ? Optional.empty()
                : findById(scheduleId);
    }

    @Override
    public void deleteSchedule(Long id) {
        jdbcTemplate.update("DELETE FROM schedule WHERE id = ?", id);
    }

    // 수정 또는 삭제하려는 일정의 비밀번호 검증
    @Override
    public String getPassword(Long scheduleId, String password) {
        return jdbcTemplate.queryForObject(
                "SELECT password FROM schedule WHERE id = ?",
                String.class,
                scheduleId
        );
    }

    // SQL 결과 -> 자바 객체 매핑을 수행하는 메소드
    private RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) -> {
            User user = User.of(
                    rs.getLong("user_id"),
                    rs.getString("name"),
                    rs.getString("email")
            );

            return new Schedule(
                    rs.getLong("id"),
                    user,
                    rs.getString("password"),
                    rs.getString("task"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("modified_at").toLocalDateTime()
            );
        };
    }

}
