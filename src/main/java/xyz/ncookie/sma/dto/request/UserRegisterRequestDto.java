package xyz.ncookie.sma.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDto(
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50) @Email String email
) {
}
