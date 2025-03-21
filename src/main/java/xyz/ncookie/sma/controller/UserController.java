package xyz.ncookie.sma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.dto.response.UserInfoResponseDto;
import xyz.ncookie.sma.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserInfoResponseDto> register(@RequestBody UserRegisterRequestDto dto) {
        UserInfoResponseDto userInfoResponseDto = userService.registerUser(dto);
        return new ResponseEntity<>(userInfoResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> findUser(@PathVariable Long userId) {
        UserInfoResponseDto userInfoResponseDto = userService.findUserById(userId);
        return new ResponseEntity<>(userInfoResponseDto, HttpStatus.OK);
    }

}
