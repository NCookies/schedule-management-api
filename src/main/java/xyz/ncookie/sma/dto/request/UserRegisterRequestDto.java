package xyz.ncookie.sma.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 유저 등록 요청 DTO
public record UserRegisterRequestDto(
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 50) @Email(message = "올바르지 않은 형식의 이메일입니다.") String email
) {
}
