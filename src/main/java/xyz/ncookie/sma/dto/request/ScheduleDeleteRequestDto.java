package xyz.ncookie.sma.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 일정 삭제 요청 DTO
public record ScheduleDeleteRequestDto(
        @NotBlank @Size(max = 50) String password
) {
}
