package xyz.ncookie.sma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    @Id
    private Long id;

    private User user;
    private String password;
    private String task;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
