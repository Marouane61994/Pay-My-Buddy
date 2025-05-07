package com.projet_6.pay_my_buddy.tests_integration;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import com.projet_6.pay_my_buddy.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    public void setUp() {


        sender = new User();
        sender.setEmail("sender@example.com");
        sender.setPassword("password123");
        sender.setUsername("senderUser");
        sender.setBalance(100.0);
        userRepository.save(sender);

        receiver = new User();
        receiver.setEmail("receiver@example.com");
        receiver.setPassword("password456");
        receiver.setUsername("receiverUser");
        receiver.setBalance(50.0);
        userRepository.save(receiver);
    }

    @Test
    @WithMockUser(username = "sender@example.com", roles = "USER")
    public void testSendMoney_Success() throws Exception {
        mockMvc.perform(post("/transactions/send")
                        .param("receiverEmail", "receiver@example.com")
                        .param("amount", "30.0")
                        .param("description", "Payment for services")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/send"));

        // Vérification en base
        User updatedSender = userRepository.findByEmail("sender@example.com").orElseThrow();
        User updatedReceiver = userRepository.findByEmail("receiver@example.com").orElseThrow();

        assertThat(updatedSender.getBalance()).isEqualTo(70.0);
        assertThat(updatedReceiver.getBalance()).isEqualTo(80.0);
        assertThat(transactionRepository.findAll()).hasSize(1);
    }

    @Test
    @WithMockUser(username = "sender@example.com", roles = "USER")
    public void testSendMoney_InsufficientFunds() throws Exception {
        mockMvc.perform(post("/transactions/send")
                        .param("receiverEmail", "receiver@example.com")
                        .param("amount", "150.0")
                        .param("description", "Payment for services")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))  // ou "send" selon ta vue de fallback
                .andExpect(model().attributeExists("error"));

        // Vérification : aucun changement
        User updatedSender = userRepository.findByEmail("sender@example.com").orElseThrow();
        User updatedReceiver = userRepository.findByEmail("receiver@example.com").orElseThrow();

        assertThat(updatedSender.getBalance()).isEqualTo(100.0);
        assertThat(updatedReceiver.getBalance()).isEqualTo(50.0);
        assertThat(transactionRepository.findAll()).isEmpty();
    }
}
