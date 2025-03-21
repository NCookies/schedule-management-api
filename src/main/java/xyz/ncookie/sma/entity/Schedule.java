package xyz.ncookie.sma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private User user;
    private String password;
    private String task;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
