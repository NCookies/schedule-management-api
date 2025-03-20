package xyz.ncookie.sma.dto.request;

import java.time.LocalDateTime;

public record ScheduleRequestDto(
        String task,
        String author,
        String password,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
