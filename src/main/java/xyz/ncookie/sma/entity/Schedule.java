package xyz.ncookie.sma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    @Id @Setter
    private Long id;

    private User user;
    private String password;
    private String task;
    @Setter private LocalDateTime createdAt;
    @Setter private LocalDateTime modifiedAt;

    public static Schedule of(User user, String password, String task) {
        return new Schedule(
                null,
                user,
                password,
                task,
                null,
                null
        );
    }

}
