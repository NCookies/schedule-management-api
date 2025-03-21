package xyz.ncookie.sma.dto.request;

public record ScheduleRequestDto(
        Long userId,
        String task,
        String password
) {
}
