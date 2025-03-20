package xyz.ncookie.sma.dto.response;

import java.time.LocalDateTime;

public record ScheduleResponseDto(
        Long id,
        String task,
        String author,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
