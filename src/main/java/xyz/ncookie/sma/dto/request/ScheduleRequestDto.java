package xyz.ncookie.sma.dto.request;

public record ScheduleRequestDto(
        String task,
        String author,
        String password
) {
}
