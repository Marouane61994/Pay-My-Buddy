package com.projet_6.pay_my_buddy.tests_integration;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import com.projet_6.pay_my_buddy.service.UserService;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Data
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@hotmail.fr");
        user.setPassword("password");
        userRepository.save(user);
    }

    @Test
    void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "newuser")
                        .param("email", "new@example.com")
                        .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void testRegisterUser_EmailAlreadyUsed() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("test@mail.com");


        when(userService.findByEmail("test@mail.com")).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/users/register")
                        .param("email", "test@mail.com")
                        .param("username", "marouane")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Cet email est déjà utilisé !"));
    }


    @Test
    void testLogin_Success() throws Exception {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("1234");

        when(userService.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/login")
                        .param("email", "test@mail.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection()) // maintenant, ça devrait passer
                .andExpect(redirectedUrl("/users/profile"));
    }


    @Test
    void testLogin_WrongPassword() throws Exception {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("correctpassword");

        when(userService.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/login")
                        .param("email", "test@mail.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Mot de passe incorrect"));
    }


    @Test
    void testLogin_UserNotFound() throws Exception {
        mockMvc.perform(post("/users/login")
                        .param("email", "notfound@example.com")
                        .param("password", "any"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Utilisateur non trouvé"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/users/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }
}
