package com.projet_6.pay_my_buddy;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import com.projet_6.pay_my_buddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        userService.userRepository = userRepository;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @Rollback
    void testSaveUser() {
        // GIVEN
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("Test User");

        // WHEN
        User savedUser = userService.saveUser(user);

        // THEN
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByEmail() {
        // GIVEN
        User user = new User();
        user.setEmail("find@example.com");
        user.setUsername("Find User");
        userService.saveUser(user);

        // WHEN
        Optional<User> foundUser = userService.findByEmail("find@example.com");

        // THEN
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("Find User");
    }

    @Test
    void testGetAllUsers() {
        // GIVEN
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setUsername("User One");

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setUsername("User Two");

        userService.saveUser(user1);
        userService.saveUser(user2);

        // WHEN
        List<User> users = userService.getAllUsers();

        // THEN
        assertThat(users).hasSize(2);
    }
}
