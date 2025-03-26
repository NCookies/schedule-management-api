package xyz.ncookie.sma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ncookie.sma.dto.ResponseCode;
import xyz.ncookie.sma.entity.User;
import xyz.ncookie.sma.exception.NotFoundException;
import xyz.ncookie.sma.repository.UserRepository;

/**
 * UserService, ScheduleService 간의 관계가 존재하지 않도록 하기 위한 중간 유틸 클래스
 * 유저 정보를 조회하여 User 인스턴스를 반환한다.
 */
@Service
@RequiredArgsConstructor
public class UserRetrievalService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER_ID));
    }

}
