package com.example.lms.repository.user;

import com.example.lms.model.User;
import com.example.lms.model.User.Role;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>(Map.of(1L, new User(1L, "admin", "admin", Role.ADMIN)));
    private final Map<String, User> usersByLogin = new HashMap<>(Map.of("admin", new User(1L, "admin", "admin", Role.ADMIN)));
    private long nextId = 1;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        if (usersByLogin.containsKey(user.getLogin())) {
            throw new IllegalArgumentException("Login is already taken");
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        usersByLogin.put(user.getLogin(), user);

        return user;
    }

    @Override
    public User update(long id, User user) {
        User existingUser = users.get(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("User with id=" + id + " not found");
        }
        if (user.getLogin() != null) {
            existingUser.setLogin(user.getLogin());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        users.put(id, existingUser);
        usersByLogin.put(existingUser.getLogin(), existingUser);

        return existingUser;
    }

    @Override
    public void delete(long id) {
        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("User with id=" + id + " not found");
        }

        User user = users.get(id);
        users.remove(id);
        usersByLogin.remove(user.getLogin());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(usersByLogin.get(login));
    }
    
}
