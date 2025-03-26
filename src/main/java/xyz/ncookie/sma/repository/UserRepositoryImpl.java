package xyz.ncookie.sma.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.ncookie.sma.entity.User;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User save(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert
                .withTableName("user")
                .usingGeneratedKeyColumns("id");

        // 현재 시각
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        // DB 저장 및 ID 조회
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        parameters.put("created_at", now);
        parameters.put("modified_at", now);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        // entity 값 설정
        user.setId(key.longValue());
        user.setCreatedAt(now.toLocalDateTime());
        user.setModifiedAt(now.toLocalDateTime());

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> result = jdbcTemplate.query("SELECT * FROM user WHERE id = ?", userRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<User> updateUserName(User user) {
        int updatedRow = jdbcTemplate.update(
                "UPDATE user SET name = ?, modified_at = NOW() WHERE id = ?",
                user.getName(),
                user.getId()
        );

        return updatedRow == 0
                ? Optional.empty()
                : findById(user.getId());
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
