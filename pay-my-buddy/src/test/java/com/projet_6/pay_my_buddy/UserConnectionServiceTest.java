package com.projet_6.pay_my_buddy;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.repository.UserConnectionRepository;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserConnectionServiceTest {

    @Mock
    private UserConnectionRepository userConnectionRepository;

    @InjectMocks
    private UserConnectionService userConnectionService;

    private User user;
    private User friend;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setEmail("user@test.com");

        friend = new User();
        friend.setId(2);
        friend.setEmail("friend@test.com");
    }

    @Test
    void testAddConnection_Success() {
        when(userConnectionRepository.findByUserId(user.getId())).thenReturn(new ArrayList<>());

        userConnectionService.addConnection(user, friend);

        ArgumentCaptor<UserConnection> captor = ArgumentCaptor.forClass(UserConnection.class);
        verify(userConnectionRepository).save(captor.capture());

        UserConnection savedConnection = captor.getValue();
        assertEquals(user, savedConnection.getUser());
        assertEquals(friend, savedConnection.getFriend());
    }

    @Test
    void testAddConnection_AlreadyConnected() {
        UserConnection existingConnection = new UserConnection();
        existingConnection.setUser(user);
        existingConnection.setFriend(friend);

        List<UserConnection> existingConnections = List.of(existingConnection);
        when(userConnectionRepository.findByUserId(user.getId())).thenReturn(existingConnections);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                userConnectionService.addConnection(user, friend));

        assertEquals("Cet utilisateur est déjà votre ami.", exception.getMessage());
        verify(userConnectionRepository, never()).save(any());
    }

    @Test
    void testGetConnections() {
        List<UserConnection> connections = new ArrayList<>();
        UserConnection conn = new UserConnection();
        conn.setUser(user);
        conn.setFriend(friend);
        connections.add(conn);

        when(userConnectionRepository.findByUserId(user.getId())).thenReturn(connections);

        List<UserConnection> result = userConnectionService.getConnections(user);
        assertEquals(1, result.size());
        assertEquals(friend, result.get(0).getFriend());
    }
}
