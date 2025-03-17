package com.projet_6.pay_my_buddy.repository;

import com.projet_6.pay_my_buddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    User findByEmail(String email);


}

