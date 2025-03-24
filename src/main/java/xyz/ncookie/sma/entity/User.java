package xyz.ncookie.sma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class User {

    @Id
    private Long id;

    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static User of(Long id, String name, String email) {
        return new User(id, name, email, null, null);
    }

}
