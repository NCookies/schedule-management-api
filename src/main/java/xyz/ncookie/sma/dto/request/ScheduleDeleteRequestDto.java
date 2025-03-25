package xyz.ncookie.sma.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ScheduleDeleteRequestDto(
        @NotBlank @Size(max = 50) String password
) {
}
