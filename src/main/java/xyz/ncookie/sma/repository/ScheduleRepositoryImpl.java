package xyz.ncookie.sma.repository;

import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.entity.Schedule;
import xyz.ncookie.sma.entity.User;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    private final String QUERY_SCHEDULE_WRITER_JOIN_STRING =
            "SELECT s.*, u.id as user_id, u.name, u.email FROM schedule s INNER JOIN user u ON s.user_id = u.id";

    public ScheduleRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<ScheduleResponseDto> saveSchedule(ScheduleRequestDto dto) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id")
                // 직접 값을 입력할 필드를 명시한다.
                // 이 외의 필드들은 테이블에 설정된 default value 또는 null 값이 할당된다.
                .usingColumns("task", "user_id", "password");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", dto.task());
        parameters.put("user_id", dto.userId());
        parameters.put("password", dto.password());     // TODO: 비밀번호 암호화 해야함

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return findById(key.longValue());
    }

    // 전체 조회
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

    // ID로 조회
    @Override
    public Optional<ScheduleResponseDto> findById(Long scheduleId) {
        List<Schedule> result = jdbcTemplate.query(
                QUERY_SCHEDULE_WRITER_JOIN_STRING + " WHERE s.id = ?",
                scheduleRowMapper(),
                scheduleId
        );

        return result.stream()
                .findAny()
                .map(ScheduleResponseDto::from);
    }

    @Override
    public int updateSchedule(Long scheduleId, String task, String password) {
        return jdbcTemplate.update(
                "UPDATE schedule SET task = ?, modified_at = NOW() WHERE id = ? AND password = ?",
                task,
                scheduleId,
                password
        );
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        jdbcTemplate.update("DELETE FROM schedule WHERE id = ? AND password = ?", id, password);
    }

    // 수정 또는 삭제하려는 일정의 비밀번호 검증
    @Override
    public boolean validPassword(Long scheduleId, String password) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schedule WHERE id = ? AND password = ?",
                Integer.class,
                scheduleId,
                password
        );

        // 비밀번호가 일치한다면 true 반환
        return count == 1;
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
