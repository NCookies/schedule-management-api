package xyz.ncookie.sma.dto.response;

import xyz.ncookie.sma.entity.User;

// 유저 정보 응답 DTO
public record UserInfoResponseDto(
        Long id,
        String name,
        String email
) {

    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

}
