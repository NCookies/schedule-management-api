package xyz.ncookie.sma.repository;

import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.entity.User;

import java.util.Optional;

public interface UserRepository {

    Long save(UserRegisterRequestDto dto);

    Optional<User> findById(Long id);

    int updateUserName(Long userId, String author);

}
