package xyz.ncookie.sma.repository;

import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.entity.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> updateUserName(User user);

}
