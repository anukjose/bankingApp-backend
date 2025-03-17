package com.banking.service;

import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BankingServiceTest {

    @InjectMocks
    private BankingService bankingService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckBalance() {
        // Use int for accountId
        int accountId = 1; 
        Account account = new Account(accountId, "John", 500.0);
        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        double balance = bankingService.checkBalance(accountId);

        assertEquals(500.0, balance);
    }

    @Test
    public void testAddMoney() {
        int accountId = 1;
        Account account = new Account(accountId, "John", 500.0);
        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        bankingService.addMoney(accountId, 200.0);

        verify(accountRepository, times(1)).save(account);
        assertEquals(700.0, account.getBalance());
    }

    @Test
    public void testDeductMoney() {
        int accountId = 1;
        Account account = new Account(accountId, "John", 500.0);
        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        bankingService.deductMoney(accountId, 200.0);

        verify(accountRepository, times(1)).save(account);
        assertEquals(300.0, account.getBalance());
    }

    @Test
    public void testDeductMoneyInsufficientFunds() {
        int accountId = 1;
        Account account = new Account(accountId, "John", 100.0); // Insufficient funds
        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        bankingService.deductMoney(accountId, 200.0);

        verify(accountRepository, times(0)).save(account); // No save should be called
    }
}
