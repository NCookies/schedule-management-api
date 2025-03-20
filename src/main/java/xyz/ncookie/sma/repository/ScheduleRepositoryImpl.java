package xyz.ncookie.sma.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.entity.Schedule;

import javax.sql.DataSource;
import java.util.*;

@Repository
@Transactional
public class ScheduleRepositoryImpl implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id")
                // 직접 값을 입력할 필드를 명시한다.
                // 이 외의 필드들은 테이블에 설정된 default value 또는 null 값이 할당된다.
                .usingColumns("task", "author", "password");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", schedule.getTask());
        parameters.put("author", schedule.getAuthor());
        parameters.put("password", schedule.getPassword());     // TODO: 비밀번호 암호화 해야함

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return findScheduleByIdOrElseThrow(key.longValue());
    }

    // 전체 조회
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String modifiedDate, String author) {
        StringBuilder query = new StringBuilder("SELECT * FROM schedule WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (!modifiedDate.isBlank()) {
            query.append(" AND DATE_FORMAT(modified_at, '%Y-%m-%d') = ?");
            params.add(modifiedDate);
        }

        if (!author.isBlank()) {
            query.append(" AND author LIKE ?");
            params.add("%" + author + "%");
        }

        query.append(" ORDER BY modified_at DESC");

        return jdbcTemplate.query(query.toString(), scheduleRowMapper(), params.toArray()).stream()
                .map(ScheduleResponseDto::from)
                .toList();
    }

    // ID로 조회
    @Override
    public ScheduleResponseDto findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", scheduleRowMapper(), id);
        return result.stream()
                .findAny()
                .map(ScheduleResponseDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID입니다: " + id));
    }

    @Override
    public int updateSchedule(Long id, String task, String author, String password) {
        return jdbcTemplate.update(
                "UPDATE schedule SET task = ?, author = ?, modified_at = NOW() WHERE id = ? AND password = ?",
                task,
                author,
                id,
                password
        );
    }

    @Override
    public int deleteSchedule(Long id, String password) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ? AND password = ?", id, password);
    }

    // SQL 결과 -> 자바 객체 매핑을 수행하는 메소드
    private RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) -> new Schedule(
                rs.getLong("id"),
                rs.getString("task"),
                rs.getString("author"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("modified_at").toLocalDateTime()
        );
    }

}
