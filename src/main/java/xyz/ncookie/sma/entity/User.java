package xyz.ncookie.sma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class User {

    @Id
    @Setter
    private Long id;

    @Setter private String name;
    private String email;
    @Setter private LocalDateTime createdAt;
    @Setter private LocalDateTime modifiedAt;

    public static User of(Long id, String name, String email) {
        return new User(id, name, email, null, null);
    }

    public static User of(String name, String email) {
        return of(null, name, email);
    }

}
