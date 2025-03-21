package xyz.ncookie.sma.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
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
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
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

        return findScheduleByIdOrElseThrow(key.longValue());
    }

    // 전체 조회
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String modifiedDate, Long userId) {
        StringBuilder query = new StringBuilder(QUERY_SCHEDULE_WRITER_JOIN_STRING + " WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (!modifiedDate.isBlank()) {
            query.append(" AND DATE_FORMAT(s.modified_at, '%Y-%m-%d') = ?");
            params.add(modifiedDate);
        }

        if (userId != -1) {
            query.append(" AND user_id = ?");
            params.add(userId);
        }

        query.append(" ORDER BY s.modified_at DESC");

        return jdbcTemplate.query(query.toString(), scheduleRowMapper(), params.toArray()).stream()
                .map(ScheduleResponseDto::from)
                .toList();
    }

    // ID로 조회
    @Override
    public ScheduleResponseDto findScheduleByIdOrElseThrow(Long scheduleId) {
        List<Schedule> result = jdbcTemplate.query(
                QUERY_SCHEDULE_WRITER_JOIN_STRING + " WHERE s.id = ?",
                scheduleRowMapper(),
                scheduleId
        );
        return result.stream()
                .findAny()
                .map(ScheduleResponseDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID입니다: " + scheduleId));
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
    public int deleteSchedule(Long id, String password) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ? AND password = ?", id, password);
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
