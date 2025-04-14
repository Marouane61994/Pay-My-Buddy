package com.projet_6.pay_my_buddy.tests_integration;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.repository.TransactionRepository;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import com.projet_6.pay_my_buddy.repository.UserConnectionRepository;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Data
@SpringBootTest
@AutoConfigureMockMvc
class UserConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    private User loggedUser;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        userConnectionRepository.deleteAll();
        userRepository.deleteAll();

        loggedUser = new User();
        loggedUser.setEmail("user@example.com");
        loggedUser.setUsername("TestUser");
        loggedUser.setPassword("123456");
        userRepository.save(loggedUser);

        session = new MockHttpSession();
        session.setAttribute("loggedUser", loggedUser);
    }


    @Test
    void testAddRelation_UserNotFound() throws Exception {
        mockMvc.perform(post("/connections/add-relation")
                        .param("friendEmail", "notfound@example.com")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("addrelation"))
                .andExpect(model().attribute("error", "Aucun utilisateur avec cet email."));
    }

    @Test
    void testAddRelation_AlreadyConnected() throws Exception {

        User friend = new User();
        friend.setEmail("friend@example.com");
        friend.setPassword("friend");
        friend.setUsername("Friend");
        userRepository.save(friend);


        UserConnection connection = new UserConnection();
        connection.setUser(loggedUser);
        connection.setFriend(friend);

        userConnectionRepository.save(connection);


        mockMvc.perform(post("/connections/add-relation")
                        .param("friendEmail", "friend@example.com")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("addrelation"))
                .andExpect(model().attribute("info", "Vous êtes déjà connecté à cet utilisateur."));
    }

    @Test
    void testAddRelation_Success() throws Exception {
        // Crée un ami à ajouter
        User newFriend = new User();
        newFriend.setEmail("newfriend@example.com");
        newFriend.setPassword("newpass");
        newFriend.setUsername("NewFriend");
        userRepository.save(newFriend);

        mockMvc.perform(post("/connections/add-relation")
                        .param("friendEmail", "newfriend@example.com")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("addrelation"))
                .andExpect(model().attribute("success", "Ami ajouté avec succès !"));
    }
}
