package xyz.ncookie.sma.dto.response;

import xyz.ncookie.sma.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponseDto(
        Long id,
        String task,
        String author,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static ScheduleResponseDto of(Long id,
                                         String task,
                                         String author,
                                         LocalDateTime createdAt,
                                         LocalDateTime modifiedAt) {
        return new ScheduleResponseDto(id, task, author, createdAt, modifiedAt);
    }

    public static ScheduleResponseDto from(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTask(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

}
