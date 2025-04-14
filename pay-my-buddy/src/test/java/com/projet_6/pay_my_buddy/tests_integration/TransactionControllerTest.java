package com.projet_6.pay_my_buddy.tests_integration;



import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.TransactionService;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
import com.projet_6.pay_my_buddy.service.UserService;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Data
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserConnectionService userConnectionService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("user@example.com");

        when(userService.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    void testShowTransactions() throws Exception {
        when(transactionService.getUserTransactions(mockUser)).thenReturn(List.of());
        when(userConnectionService.getFriendsForUser(mockUser)).thenReturn(List.of());

        mockMvc.perform(get("/transactions/send"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("relations"));
    }

    @Test
    void testSendMoneySuccess() throws Exception {
        when(transactionService.sendMoney(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble(), Mockito.anyString()))
                .thenReturn(true);

        mockMvc.perform(post("/transactions/send")
                        .param("receiverEmail", "friend@example.com")
                        .param("amount", "50.0")
                        .param("description", "test transaction"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/send"));
    }

    @Test
    void testSendMoneyFailure() throws Exception {
        when(transactionService.sendMoney(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble(), Mockito.anyString()))
                .thenReturn(false);

        when(transactionService.getUserTransactions(mockUser)).thenReturn(List.of());
        when(userConnectionService.getFriendsForUser(mockUser)).thenReturn(List.of());

        mockMvc.perform(post("/transactions/send")
                        .param("receiverEmail", "friend@example.com")
                        .param("amount", "0")
                        .param("description", "test fail"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("error"));
    }
}
