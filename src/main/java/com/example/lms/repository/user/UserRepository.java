package com.example.lms.repository.user;

import java.util.Optional;

import com.example.lms.model.User;
import com.example.lms.repository.base.Repository;

public interface UserRepository extends Repository<User>{
    Optional<User> findByLogin(String login);
}
