package com.projet_6.pay_my_buddy.tests_integration;


import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setEmail("testuser@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = "USER")
    public void testRegisterUserSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .param("email", "newuser@example.com")
                        .param("username", "newuser")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/login"));
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = "USER")
    public void testRegisterUserEmailAlreadyUsed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .param("email", "testuser@example.com") // Email déjà utilisé
                        .param("username", "anotheruser")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", "Cet email est déjà utilisé !"));
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = "USER")
    public void testUpdateProfileSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/profile/edit")
                        .param("username", "updatedUser")
                        .param("email", "updateduser@example.com")
                        .param("password", "newpassword123"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("success"))
                .andExpect(MockMvcResultMatchers.model().attribute("success", "Profil mis à jour !"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", userService.findByEmail("testuser@example.com").get()));
    }
}
