package xyz.ncookie.sma.repository;

import xyz.ncookie.sma.dto.request.UserRegisterRequestDto;
import xyz.ncookie.sma.entity.User;

public interface UserRepository {

    User save(UserRegisterRequestDto dto);

    User findById(Long id);

    int updateUserName(Long userId, String author);

}
