package xyz.ncookie.sma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.dto.response.UserInfoResponseDto;
import xyz.ncookie.sma.entity.User;
import xyz.ncookie.sma.exception.NoSuchIdException;
import xyz.ncookie.sma.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserInfoResponseDto registerUser(UserRegisterRequestDto dto) {
        Long savedUserId = userRepository.save(dto);
        return UserInfoResponseDto.from(findUser(savedUserId));
    }

    @Transactional(readOnly = true)
    @Override
    public UserInfoResponseDto findUserById(Long userId) {
        return UserInfoResponseDto.from(findUser(userId));
    }

    private User findUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NoSuchIdException("존재하지 않는 회원의 ID 입니다."));
    }

}
