package com.unitech.app.unitechapp.repository;


import com.unitech.app.unitechapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPin(String pin);
    Optional<User> findByPinAndPassword(String pin, String password);
}
