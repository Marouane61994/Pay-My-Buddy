package com.projet_6.pay_my_buddy.repository;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer> {
    List<UserConnection> findByUserId(int userId);
    boolean existsByUserAndFriend(User user, User friend);

}