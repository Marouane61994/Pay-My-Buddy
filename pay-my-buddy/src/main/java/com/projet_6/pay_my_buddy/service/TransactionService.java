package com.projet_6.pay_my_buddy.service;

import com.projet_6.pay_my_buddy.model.Transaction;
import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.repository.TransactionRepository;
import com.projet_6.pay_my_buddy.repository.UserConnectionRepository;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private TransactionRepository transactionRepository;



    public String sendMoney(int senderId, int receiverId, Double amount) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();

        List<UserConnection> connections = userConnectionRepository.findByUserId(senderId);
        boolean isFriend = connections.stream()
                .anyMatch(conn -> Objects.equals(conn.getFriend().getId(), receiverId));

        if (!isFriend) return "Le destinataire n'est pas un ami de l'expéditeur.";
        if (sender.getBalance() < amount) return "Solde insuffisant.";



        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());

        transactionRepository.save(tx);

        return "Transaction réussie.";
    }


    }


