package com.projet_6.pay_my_buddy.tests_unitaires;

import com.projet_6.pay_my_buddy.model.Transaction;
import com.projet_6.pay_my_buddy.model.User;


import com.projet_6.pay_my_buddy.repository.TransactionRepository;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import com.projet_6.pay_my_buddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    void testSendMoney_SuccessfulTransaction() {
        User sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");
        sender.setBalance(100.0);

        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");
        receiver.setBalance(20.0);

        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", 50.0, "Payment");

        assertTrue(result);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction savedTransaction = captor.getValue();
        assertEquals(sender, savedTransaction.getSender());
        assertEquals(receiver, savedTransaction.getReceiver());
        assertEquals(50.0, savedTransaction.getAmount());
        assertEquals("Payment", savedTransaction.getDescription());

        assertEquals(50.0, sender.getBalance());   // 100 - 50
        assertEquals(70.0, receiver.getBalance()); // 20 + 50
    }


    @Test
    void testSendMoney_SenderNotFound() {
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.empty());

        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", 20.0, "Test");
        assertFalse(result);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testSendMoney_ReceiverNotFound() {
        User sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");

        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.empty());

        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", 20.0, "Test");
        assertFalse(result);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testSendMoney_InvalidAmount() {
        User sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");

        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", -10.0, "Test");
        assertFalse(result);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testSendMoney_SenderHasIdZero() {
        User sender = new User();
        sender.setId(0);
        sender.setEmail("sender@example.com");

        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", 10.0, "Test");
        assertFalse(result);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testSendMoney_NotEnoughBalance() {
        User sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");
        sender.setBalance(5.0);

        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");
        receiver.setBalance(0.0);

        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        // Act
        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", 10.0, "Paiement");


        assertFalse(result);
        verify(transactionRepository, never()).save(any());
    }

}