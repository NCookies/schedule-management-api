package xyz.ncookie.sma.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 일정 등록 요청 DTO
public record ScheduleRequestDto(
        @NotNull Long userId,
        @NotBlank @Size(max = 200) String task,
        @NotBlank @Size(max = 255) String password
) {
}
