package xyz.ncookie.sma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.ncookie.sma.common.ApiResponse;
import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.dto.response.UserInfoResponseDto;
import xyz.ncookie.sma.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<Object> register(@RequestBody UserRegisterRequestDto dto) {
        UserInfoResponseDto userInfoResponseDto = userService.registerUser(dto);
        return ApiResponse.success(HttpStatus.CREATED, userInfoResponseDto);
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserInfoResponseDto> findUser(@PathVariable Long userId) {
        UserInfoResponseDto userInfoResponseDto = userService.findUserById(userId);
        return ApiResponse.ok(userInfoResponseDto);
    }

}
