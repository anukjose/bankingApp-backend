
package com.banking.service;

import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankingService {
    private static final Logger logger = LoggerFactory.getLogger(BankingService.class);
    private final AccountRepository accountRepository;

    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public synchronized double checkBalance(int accountId) {
        logger.debug("Checking balance for accountId: {}", accountId);
        Optional<Account> accountOpt = accountRepository.findById( accountId);
        return accountOpt.map(Account::getBalance).orElse(0.0);
    }

    @Transactional
    public synchronized void addMoney(int accountId, double amount) {
        Optional<Account> accountOpt = accountRepository.findById( accountId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
        }
    }

    @Transactional
    public synchronized void deductMoney(int accountId, double amount) {
        Optional<Account> accountOpt = accountRepository.findById( accountId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);
            }
        }
    }
}

/* old code
package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import org.springframework.stereotype.Service;

@Service
public class BankingService {

    private final AccountDAO accountDAO;

    public BankingService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public synchronized double checkBalance(int accountId) {
        Account account = accountDAO.getAccountById(accountId);
        return account != null ? account.getBalance() : 0;
    }

    public synchronized void addMoney(int accountId, double amount) {
        Account account = accountDAO.getAccountById(accountId);
        if (account != null) {
            double newBalance = account.getBalance() + amount;
            accountDAO.updateAccountBalance(accountId, newBalance);
        }
    }

    public synchronized void deductMoney(int accountId, double amount) {
        Account account = accountDAO.getAccountById(accountId);
        if (account != null && account.getBalance() >= amount) {
            double newBalance = account.getBalance() - amount;
            accountDAO.updateAccountBalance(accountId, newBalance);
        }
    }
}
*/