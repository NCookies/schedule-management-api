package xyz.ncookie.sma.dto.request;

public record ScheduleUpdateRequestDto(
        String task,
        String author,
        String password
) {
}
