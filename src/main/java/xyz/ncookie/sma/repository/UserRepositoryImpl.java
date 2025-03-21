package xyz.ncookie.sma.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.entity.User;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User save(UserRegisterRequestDto dto) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("user")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "email");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", dto.name());
        parameters.put("email", dto.email());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return findById(key.longValue());
    }

    @Override
    public User findById(Long id) {
        List<User> result = jdbcTemplate.query("SELECT * FROM user WHERE id = ?", userRowMapper(), id);
        return result.stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID입니다: " + id));
    }

    @Override
    public int updateUserName(Long userId, String author) {
        return jdbcTemplate.update(
                "UPDATE user SET name = ?, modified_at = NOW() WHERE id = ?",
                author,
                userId
        );
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("modified_at").toLocalDateTime()
        );
    }

}
