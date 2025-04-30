package com.projet_6.pay_my_buddy.tests_unitaires;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.repository.UserConnectionRepository;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserConnectionServiceTest {

    @Mock
    private UserConnectionRepository userConnectionRepository;

    @InjectMocks
    private UserConnectionService userConnectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddConnection_Success() {
        User user = new User();
        user.setId(1);

        User friend = new User();
        friend.setId(2);

        when(userConnectionRepository.findByUserId(user.getId())).thenReturn(new ArrayList<>());

        userConnectionService.addConnection(user, friend);

        ArgumentCaptor<UserConnection> captor = ArgumentCaptor.forClass(UserConnection.class);
        verify(userConnectionRepository).save(captor.capture());

        UserConnection saved = captor.getValue();
        assertEquals(user, saved.getUser());
        assertEquals(friend, saved.getFriend());
    }

    @Test
    void testAddConnection_AlreadyConnected_ThrowsException() {
        User user = new User();
        user.setId(1);

        User friend = new User();
        friend.setId(2);

        UserConnection existingConnection = new UserConnection();
        existingConnection.setUser(user);
        existingConnection.setFriend(friend);

        when(userConnectionRepository.findByUserId(user.getId()))
                .thenReturn(List.of(existingConnection));

        Exception exception = assertThrows(IllegalStateException.class, () ->
                userConnectionService.addConnection(user, friend));

        assertEquals("Cet utilisateur est déjà votre ami.", exception.getMessage());
        verify(userConnectionRepository, never()).save(any());
    }


    @Test
    void testGetFriendsForUser() {
        User user = new User();
        user.setId(1);

        User friend1 = new User();
        friend1.setId(2);

        User friend2 = new User();
        friend2.setId(3);

        UserConnection conn1 = new UserConnection();
        conn1.setUser(user);
        conn1.setFriend(friend1);

        UserConnection conn2 = new UserConnection();
        conn2.setUser(user);
        conn2.setFriend(friend2);

        when(userConnectionRepository.findByUserId(user.getId()))
                .thenReturn(List.of(conn1, conn2));

        List<User> friends = userConnectionService.getFriendsForUser(user);

        assertEquals(2, friends.size());
        assertTrue(friends.contains(friend1));
        assertTrue(friends.contains(friend2));
    }
}
