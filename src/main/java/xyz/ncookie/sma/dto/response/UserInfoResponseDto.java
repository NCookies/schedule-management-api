package xyz.ncookie.sma.dto.response;

import xyz.ncookie.sma.entity.User;

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
