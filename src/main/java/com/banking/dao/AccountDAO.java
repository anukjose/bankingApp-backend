package com.banking.dao;

import com.banking.model.AccountOld;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
//this class no longer need as repository class added for jpa
@Repository
public class AccountDAO {

    private final Map<Integer, AccountOld> accountMap = new HashMap<>();

    public AccountDAO() {
        accountMap.put(1, new AccountOld(1, 1000.0)); // Sample account
    }

    public AccountOld getAccountById(int accountId) {
        return accountMap.get(accountId);
    }

    public void updateAccountBalance(int accountId, double newBalance) {
    	AccountOld account = accountMap.get(accountId);
        if (account != null) {
            account.setBalance(newBalance);
        }
    }
}
