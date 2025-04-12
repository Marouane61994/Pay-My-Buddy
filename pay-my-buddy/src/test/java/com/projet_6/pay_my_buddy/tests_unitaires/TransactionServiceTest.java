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

import java.util.Arrays;
import java.util.List;
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
    void testGetUserTransactions() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        when(transactionRepository.findBySenderOrReceiver(user, user))
                .thenReturn(Arrays.asList(t1, t2));

        List<Transaction> transactions = transactionService.getUserTransactions(user);

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findBySenderOrReceiver(user, user);
    }

    @Test
    void testSendMoney_SuccessfulTransaction() {
        User sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");

        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

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
}
