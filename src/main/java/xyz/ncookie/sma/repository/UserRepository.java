package xyz.ncookie.sma.repository;

import xyz.ncookie.sma.entity.User;

public interface UserRepository {

    User findById(Long id);

    int updateUserName(Long userId, String author);

}
