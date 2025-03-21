package xyz.ncookie.sma.dto.request;

public record ScheduleUpdateRequestDto(
        Long userId,
        String task,
        String author,
        String password
) {
}
