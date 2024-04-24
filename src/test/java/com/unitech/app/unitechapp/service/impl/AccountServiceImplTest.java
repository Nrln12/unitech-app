package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.entity.Account;
import com.unitech.app.unitechapp.entity.User;
import com.unitech.app.unitechapp.model.request.AccountRequest;
import com.unitech.app.unitechapp.model.request.MoneyTransferRequest;
import com.unitech.app.unitechapp.model.response.AccountResponse;
import com.unitech.app.unitechapp.model.response.UserResponse;
import com.unitech.app.unitechapp.repository.AccountRepository;
import com.unitech.app.unitechapp.repository.UserRepository;
import com.unitech.app.unitechapp.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    AccountServiceImpl accountService;
    @Mock
    private User user;
    private UserResponse userResponse;
    private Account account;
    private Account fromAcc;
    private Account toAcc;
    private AccountRequest accountRequest;
    private AccountResponse accountResponse;
    private MoneyTransferRequest moneyTransferRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .pin("1234")
                .password("1234")
                .accounts(null)
                .createdAt(LocalDateTime.now())
                .build();
        userResponse = UserResponse.builder()
                .firstname("John")
                .lastname("Doe")
                .pin("1234")
                .build();
        account = Account.builder()
                .accountNo("A12345")
                .user(user)
                .build();
        fromAcc = Account.builder()
                .accountNo("A1234")
                .balance(123.3)
                .status(true)
                .user(user)
                .build();
        toAcc = Account.builder()
                .accountNo("B1234")
                .balance(45.8)
                .status(true)
                .user(user)
                .build();
        accountRequest = AccountRequest
                .builder()
                .pin("1234")
                .accountNo("A12345")
                .build();
        accountResponse = AccountResponse
                .builder()
                .accountNo("A12345")
                .balance(0.0)
                .user(userResponse)
                .build();
        moneyTransferRequest = MoneyTransferRequest
                .builder()
                .fromAccountNo(fromAcc.getAccountNo())
                .toAccountNo(toAcc.getAccountNo())
                .amount(5.0)
                .build();
    }

    @Test
    public void testCreateAccount() {
        when(userRepository.findByPin("1234")).thenReturn(Optional.of(user));
        when(accountRepository.findAccountByAccountNo("A12345")).thenReturn(Optional.empty());
        when(modelMapper.map(accountRequest, Account.class)).thenReturn(account);
        assertDoesNotThrow(() -> accountService.createAccount(accountRequest));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testGetAccountByPin() {
        String token = "Bearer 1234";
        String pin = user.getPin();
        Long userId = user.getId();

        when(jwtService.extractUserName("1234")).thenReturn(pin);
        when(userRepository.findByPin(pin)).thenReturn(Optional.of(user));

        List<Account> accounts = Collections.singletonList(account);
        when(accountRepository.findAccountByUserIdAndStatusIsTrue(userId)).thenReturn(accounts);

        AccountResponse[] expectedResponses = {accountResponse};
        when(modelMapper.map(accounts, AccountResponse[].class)).thenReturn(expectedResponses);

        List<AccountResponse> result = accountService.getAccountsByPin(token);

        assertNotNull(result);

        assertEquals(Arrays.asList(expectedResponses), result);
        verify(jwtService).extractUserName("1234");
        verify(userRepository).findByPin(pin);
        verify(accountRepository).findAccountByUserIdAndStatusIsTrue(userId);
        verify(modelMapper).map(accounts, AccountResponse[].class);
    }

    @Test
    public void testMoneyTransfer() {
        Double fromAccBalance = fromAcc.getBalance();
        Double toAccBalance = toAcc.getBalance();
        Double amount = moneyTransferRequest.getAmount();
        AccountResponse updatedExpectedFromAcc = AccountResponse
                .builder()
                .accountNo("A1234")
                .balance(fromAccBalance - amount)
                .user(userResponse)
                .build();

        AccountResponse updatedExpectedToAcc = AccountResponse
                .builder()
                .accountNo("B1234")
                .balance(toAccBalance + amount)
                .user(userResponse)
                .build();
        when(accountRepository.findAccountByAccountNoAndStatusIsTrue(fromAcc.getAccountNo())).thenReturn(Optional.of(fromAcc));
        when(accountRepository.findAccountByAccountNoAndStatusIsTrue(toAcc.getAccountNo())).thenReturn(Optional.of(toAcc));

        accountService.moneyTransfer(moneyTransferRequest);
        assertEquals(updatedExpectedFromAcc.getBalance(), fromAcc.getBalance());
        assertEquals(updatedExpectedToAcc.getBalance(), toAcc.getBalance());
        verify(accountRepository).findAccountByAccountNoAndStatusIsTrue(fromAcc.getAccountNo());
        verify(accountRepository).findAccountByAccountNoAndStatusIsTrue(toAcc.getAccountNo());
    }
}

