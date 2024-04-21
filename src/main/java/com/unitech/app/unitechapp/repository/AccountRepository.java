package com.unitech.app.unitechapp.repository;


import com.unitech.app.unitechapp.entity.Account;
import com.unitech.app.unitechapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountNo(String accountNo);
    Optional<Account> findAccountByAccountNoAndStatusIsTrue(String accountNo);
    List<Account> findAccountByUserIdAndStatusIsTrue(Long id);
}
