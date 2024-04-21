package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.entity.Account;
import com.unitech.app.unitechapp.entity.User;
import com.unitech.app.unitechapp.exception.AlreadyExistsException;
import com.unitech.app.unitechapp.exception.BadRequestException;
import com.unitech.app.unitechapp.exception.NotFoundException;
import com.unitech.app.unitechapp.model.request.AccountRequest;
import com.unitech.app.unitechapp.model.request.MoneyTransferRequest;
import com.unitech.app.unitechapp.model.response.AccountResponse;
import com.unitech.app.unitechapp.repository.AccountRepository;
import com.unitech.app.unitechapp.repository.UserRepository;
import com.unitech.app.unitechapp.service.AccountService;
import com.unitech.app.unitechapp.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public void createAccount(AccountRequest request) {
        Optional<User> optUser = Optional.ofNullable(userRepository.findByPin(request.getPin())
                .orElseThrow(() -> new NotFoundException("User with this pin doesn't exist")));
        Optional<Account> optAccount = accountRepository.findAccountByAccountNo(request.getAccountNo());
        if (optAccount.isPresent())
            throw new AlreadyExistsException("This account no already exists.");
        Account account = modelMapper.map(request, Account.class);
        account.setUser(optUser.get());
        account.setBalance(0.0);
        account.setStatus(true);
        account.setCreatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public List<AccountResponse> getAccountsByPin(String token) {
        String pin = jwtService.extractUserName(token.substring(7));
        Optional<User> optUser = Optional.ofNullable(userRepository.findByPin(pin)
                .orElseThrow(() -> new NotFoundException("User with this pin doesn't exist")));
        List<Account> accounts = accountRepository.findAccountByUserIdAndStatusIsTrue(optUser.get().getId());
        if (accounts.isEmpty()) {
            throw new NotFoundException("No active account with given pin");
        }
        return Arrays.asList(modelMapper.map(accounts, AccountResponse[].class));
    }

    @Override
    public void moneyTransfer(MoneyTransferRequest request) {
        validateMoneyTransferRequest(request.getFromAccountNo(),"you should enter the source account");
        validateMoneyTransferRequest(request.getToAccountNo(),"you should enter the target account");

        Account fromAccount = getActiveAccountByAccountNo(request.getFromAccountNo());
        Account toAccount = getActiveAccountByAccountNo(request.getToAccountNo());

        if (fromAccount == null) {
            throw new NotFoundException("Your account is not active or doesn't exist.");
        }
        if (toAccount == null) {
            throw new NotFoundException("The account you want to transfer is not active or doesn't exist.");
        }
        if (fromAccount.equals(toAccount)) {
            throw new BadRequestException("You can not transfer money to the same account.");
        }

        if (request.getAmount() <= 0) {
            throw new BadRequestException("Invalid amount for transfer.");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BadRequestException("You don't have enough money for transfer");
        }
        performMoneyTransfer(fromAccount, toAccount, request.getAmount());
    }

    private void validateMoneyTransferRequest(String accountNo, String message) {
        if (StringUtils.isEmpty(accountNo)) {
            throw new BadRequestException(message);
        }
    }

    private Account getActiveAccountByAccountNo(String accountNo) {
        Optional<Account> accountOptional = accountRepository
                .findAccountByAccountNoAndStatusIsTrue(accountNo);
        return accountOptional.orElse(null);
    }

    @Transactional
    protected void performMoneyTransfer(Account fromAccount, Account toAccount, Double amount) {
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE)
//    public void moneyTransfer(MoneyTransferRequest request) {
//        if (request.getFromAccountNo().isEmpty())
//            throw new BadRequestException("From account can not be null");
//        if (request.getToAccountNo().isEmpty())
//            throw new BadRequestException("To account can not be null");
//        if (request.getAmount() == null || request.getAmount().intValue() == 0)
//            throw new BadRequestException("Invalid amount");
//        Optional<Account> fromAcc = accountRepository.findAccountByAccountNoAndStatusIsTrue(request.getFromAccountNo());
//        Optional<Account> toAcc = accountRepository.findAccountByAccountNoAndStatusIsTrue(request.getToAccountNo());
//
//        if (fromAcc.isPresent()) {
//            if (toAcc.isPresent()) {
//                if (!Objects.equals(toAcc.get().getAccountNo(), fromAcc.get().getAccountNo())) {
//                    if (request.getAmount() <= fromAcc.get().getBalance()) {
//                        fromAcc.get().setBalance(fromAcc.get().getBalance() - request.getAmount());
//                        toAcc.get().setBalance(toAcc.get().getBalance() + request.getAmount());
//                    } else {
//                        throw new BadRequestException("You don't have enough money for transfer");
//                    }
//                } else {
//                    throw new BadRequestException("You can't transfer money to the same account");
//                }
//
//            } else {
//                throw new NotFoundException("There is no active account you want to transfer money");
//            }
//        } else {
//            throw new NotFoundException("There is no active account for transfer money");
//        }
//
//    }
}
