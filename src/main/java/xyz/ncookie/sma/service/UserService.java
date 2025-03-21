package xyz.ncookie.sma.service;

import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.dto.response.UserInfoResponseDto;

public interface UserService {

    UserInfoResponseDto registerUser(UserRegisterRequestDto dto);

    UserInfoResponseDto findUserById(Long userId);

}
