package com.projet_6.pay_my_buddy.tests_unitaires;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import com.projet_6.pay_my_buddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("test2@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("test1@example.com", users.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByEmail_UserExists() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("user@example.com");

        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("newuser@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.saveUser(user);

        assertEquals("newuser@example.com", saved.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetCurrentUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("current@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        User currentUser = userService.getCurrentUser();

        assertEquals("current@example.com", currentUser.getEmail());
        verify(userRepository, times(1)).findAll();
    }
}
