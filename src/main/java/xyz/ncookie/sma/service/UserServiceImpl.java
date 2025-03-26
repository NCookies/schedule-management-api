package xyz.ncookie.sma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.dto.response.UserInfoResponseDto;
import xyz.ncookie.sma.entity.User;
import xyz.ncookie.sma.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRetrievalService userRetrievalService;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserInfoResponseDto registerUser(UserRegisterRequestDto dto) {
        User savedUser = userRepository.save(
                User.of(dto.name(), dto.email())
        );

        return UserInfoResponseDto.from(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserInfoResponseDto findUserById(Long userId) {
        return UserInfoResponseDto.from(userRetrievalService.findUser(userId));
    }

}
