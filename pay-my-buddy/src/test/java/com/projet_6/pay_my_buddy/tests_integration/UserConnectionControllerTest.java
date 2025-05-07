package com.projet_6.pay_my_buddy.tests_integration;

import com.projet_6.pay_my_buddy.controller.UserConnectionController;
import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
import com.projet_6.pay_my_buddy.service.UserService;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserConnectionController.class)
public class UserConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserConnectionService userConnectionService;

    @MockitoBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user1 = new User();
        user1.setEmail("loggedUser@example.com");
        user1.setPassword("password123");
        user1.setUsername("loggedUser");
        user1.setBalance(100.0);

        Mockito.when(userService.getCurrentUser()).thenReturn(user1);

        User user2 = new User();
        user2.setEmail("friend@example.com");
        user2.setPassword("password456");
        user2.setUsername("friendUser");
        user2.setBalance(50.0);

        Mockito.when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(user2));
    }

    @Test
    @WithMockUser(username = "loggedUser@example.com", roles = "USER")
    public void testAddRelation_Success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/connections/add-relation")
                        .param("friendEmail", "friend@example.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())) // <-- ajout du token CSRF valide
                .andExpect(status().is3xxRedirection())  // Redirection après ajout de la relation
                .andExpect(view().name("redirect:/transactions/send"));
    }

    @Test
    @WithMockUser(username = "loggedUser@example.com", roles = "USER")
    public void testAddRelation_FriendNotFound() throws Exception {
        User user1 = new User();
        user1.setEmail("loggedUser@example.com");
        user1.setPassword("password123");
        user1.setUsername("loggedUser");
        user1.setBalance(100.0);

        Mockito.doThrow(new RuntimeException("User not found"))
                .when(userConnectionService)
                .addRelation(user1, "nonexistent@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/connections/add-relation")
                        .param("friendEmail", "nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("addrelation"))
                .andExpect(model().attributeExists("error"));
    }

}
