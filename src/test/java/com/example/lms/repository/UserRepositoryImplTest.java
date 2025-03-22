package com.example.lms.repository;

import com.example.lms.model.User;
import com.example.lms.model.User.Role;
import com.example.lms.repository.user.UserRepositoryImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {
    private UserRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryImpl();
    }

    @Test
    void testCreate_Success() {
        User user = new User(null, "login1", "password1", Role.ADMIN);
        User savedUser = repository.create(user);

        assertNotNull(savedUser);
        assertTrue(savedUser.getId() > 0);
        assertEquals("login1", savedUser.getLogin());
        assertEquals("password1", savedUser.getPassword());
        assertEquals(Role.ADMIN, savedUser.getRole());
    }

    @Test
    void testCreate_DuplicateLogin() {
        User user1 = new User(null, "login1", "password1", Role.ADMIN);
        User user2 = new User(null, "login1", "password2", Role.TEACHER);

        repository.create(user1);

        assertThrows(IllegalArgumentException.class, () -> repository.create(user2));
    }

    @Test
    void testGetAll_Success() {
        repository.create(new User(null, "login1", "password1", Role.ADMIN));
        repository.create(new User(null, "login2", "password2", Role.TEACHER));

        List<User> users = repository.getAll();

        assertEquals(2, users.size());
    }

    @Test
    void testGetById_Success() {
        User user = repository.create(new User(null, "login1", "password1", Role.ADMIN));

        Optional<User> foundUser = repository.get(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("login1", foundUser.get().getLogin());
        assertEquals("password1", foundUser.get().getPassword());
        assertEquals(Role.ADMIN, foundUser.get().getRole());
    }

    @Test
    void testGetById_NotFound() {
        Optional<User> foundUser = repository.get(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUpdate_Success() {
        User user = repository.create(new User(null, "login1", "password1", Role.ADMIN));
        user.setLogin("newLogin");
        user.setPassword("newPassword");
        user.setRole(Role.TEACHER);

        User updatedUser = repository.update(user.getId(), user);

        assertEquals("newLogin", updatedUser.getLogin());
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals(Role.TEACHER, updatedUser.getRole());
    }

    @Test
    void testUpdate_NotFound() {
        User user = new User(999L, "login1", "password1", Role.ADMIN);

        assertThrows(IllegalArgumentException.class, () -> repository.update(999L, user));
    }

    @Test
    void testDelete_Success() {
        User user = repository.create(new User(null, "login1", "password1", Role.ADMIN));

        repository.delete(user.getId());

        assertFalse(repository.get(user.getId()).isPresent());
        assertFalse(repository.findByLogin("login1").isPresent());
    }

    @Test
    void testDelete_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> repository.delete(999L));
    }

    @Test
    void testFindByLogin_Success() {
        repository.create(new User(null, "login1", "password1", Role.ADMIN));

        Optional<User> foundUser = repository.findByLogin("login1");

        assertTrue(foundUser.isPresent());
        assertEquals("login1", foundUser.get().getLogin());
        assertEquals("password1", foundUser.get().getPassword());
        assertEquals(Role.ADMIN, foundUser.get().getRole());
    }

    @Test
    void testFindByLogin_NotFound() {
        Optional<User> foundUser = repository.findByLogin("nonExistentLogin");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testDelete_StudentRole() {
        User student = repository.create(new User(null, "student1", "password1", Role.STUDENT));

        repository.delete(student.getId());

        assertFalse(repository.get(student.getId()).isPresent());
        assertFalse(repository.findByLogin("student1").isPresent());
    }
}