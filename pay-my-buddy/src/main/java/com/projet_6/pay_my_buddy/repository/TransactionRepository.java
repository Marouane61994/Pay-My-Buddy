package com.projet_6.pay_my_buddy.repository;

import com.projet_6.pay_my_buddy.model.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySenderId(int senderId);
    List<Transaction> findByReceiverId(int receiverId);
}

