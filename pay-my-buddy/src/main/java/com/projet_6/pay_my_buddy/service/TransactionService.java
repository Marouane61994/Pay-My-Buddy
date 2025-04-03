package com.projet_6.pay_my_buddy.service;


import com.projet_6.pay_my_buddy.model.Transaction;
import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.TransactionRepository;
import com.projet_6.pay_my_buddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findBySenderOrReceiver(user, user);
    }

    public boolean sendMoney(String senderEmail, String receiverEmail, double amount, String description) {
        Optional<User> senderOpt = userRepository.findByEmail(senderEmail);
        Optional<User> receiverOpt = userRepository.findByEmail(receiverEmail);

        if (senderOpt.isPresent() && receiverOpt.isPresent() && amount > 0) {
            User sender = senderOpt.get();
            User receiver = receiverOpt.get();

            // Simuler un solde pour le moment (ajouter une gestion du solde plus tard)
            if (sender.getId() == 0) {

                return false; // Impossible d'envoyer à soi-même
            }

            Transaction transaction = new Transaction();
            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transaction.setTimestamp(LocalDateTime.now());

            transactionRepository.save(transaction);
            return true;
        }
        return false;
    }
}



