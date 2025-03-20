package xyz.ncookie.sma.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private String task;
    private String author;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Schedule from(ScheduleRequestDto dto) {
        return new Schedule(
                null,
                dto.task(),
                dto.author(),
                dto.password(),
                null,
                null
        );
    }

}
