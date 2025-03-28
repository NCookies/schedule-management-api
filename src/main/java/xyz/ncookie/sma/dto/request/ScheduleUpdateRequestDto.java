package xyz.ncookie.sma.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 일정 수정 요청 API
public record ScheduleUpdateRequestDto(
        @NotNull Long userId,
        @NotBlank @Size(max = 200)  String task,
        @NotBlank @Size(max = 50) String author,
        @NotBlank @Size(max = 50) String password
) {
}
