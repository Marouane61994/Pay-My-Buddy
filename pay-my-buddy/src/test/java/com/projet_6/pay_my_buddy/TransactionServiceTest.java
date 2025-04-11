package com.projet_6.pay_my_buddy;

import com.projet_6.pay_my_buddy.model.Transaction;
import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.TransactionRepository;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import com.projet_6.pay_my_buddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");

        receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");
    }

    @Test
    public void testSendMoney_Success() {
        // Arrange
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        // Act
        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", 50.0, "Test payment");

        // Assert
        assertTrue(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testSendMoney_SenderEqualsReceiver() {
        // Arrange
        receiver.setId(1); // même ID que sender
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(receiver)); // simulate sending to self

        // Act
        boolean result = transactionService.sendMoney("sender@example.com", "sender@example.com", 50.0, "Self");

        // Assert
        assertFalse(result);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testSendMoney_UserNotFound() {
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", 10.0, "Invalid sender");

        assertFalse(result);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testSendMoney_InvalidAmount() {
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));

        boolean result = transactionService.sendMoney("sender@example.com", "receiver@example.com", -5.0, "Negative amount");

        assertFalse(result);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
