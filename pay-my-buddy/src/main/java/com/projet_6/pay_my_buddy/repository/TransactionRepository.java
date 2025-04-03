package com.projet_6.pay_my_buddy.repository;

import com.projet_6.pay_my_buddy.model.Transaction;

import com.projet_6.pay_my_buddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySenderOrReceiver(User sender, User receiver);
}

