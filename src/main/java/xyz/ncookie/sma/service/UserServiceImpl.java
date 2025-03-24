package xyz.ncookie.sma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.dto.response.UserInfoResponseDto;
import xyz.ncookie.sma.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserInfoResponseDto registerUser(UserRegisterRequestDto dto) {
        return UserInfoResponseDto.from(userRepository.save(dto));
    }

    @Transactional(readOnly = true)
    @Override
    public UserInfoResponseDto findUserById(Long userId) {
        return UserInfoResponseDto.from(userRepository.findById(userId));
    }

}
