package xyz.ncookie.sma.dto.response;

import xyz.ncookie.sma.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponseDto(
        Long id,
        UserInfoResponseDto userInfo,
        String task,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static ScheduleResponseDto from(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                UserInfoResponseDto.from(schedule.getUser()),
                schedule.getTask(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

}
