package com.projet_6.pay_my_buddy.tests_integration;

import com.projet_6.pay_my_buddy.controller.UserController;
import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService; // Mock du service

    @BeforeEach
    public void setUp() {
        // Configurer le mock pour retourner un utilisateur fictif lorsque la méthode 'findByEmail' est appelée
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userService.findByEmail("testuser@example.com")).thenReturn(java.util.Optional.of(user));
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = "USER")
    public void testRegisterUserSuccess() throws Exception {
        // Tester le POST pour l'enregistrement d'un utilisateur
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
        // Tester le cas où l'email est déjà utilisé
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
        // Tester la mise à jour du profil de l'utilisateur
        mockMvc.perform(MockMvcRequestBuilders.post("/users/profile/edit")
                        .param("username", "updatedUser")
                        .param("email", "updateduser@example.com")
                        .param("password", "newpassword123"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("success"))
                .andExpect(MockMvcResultMatchers.model().attribute("success", "Profil mis à jour !"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", userService.getCurrentUser()));
    }
}
