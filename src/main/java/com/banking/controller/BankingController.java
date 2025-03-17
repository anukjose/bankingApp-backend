package com.banking.controller;

import com.banking.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/checkBalance")
    public ResponseEntity<Double> checkBalance(@RequestParam int accountId) {
        double balance = bankingService.checkBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/addMoney")
    public ResponseEntity<String> addMoney(@RequestBody Map<String, Object> request) {
        int accountId = (int) request.get("accountId");
        double amount = (double) request.get("amount");
        bankingService.addMoney(accountId, amount);
        return ResponseEntity.ok("Money added successfully!");
    }

    @PostMapping("/deductMoney")
    public ResponseEntity<String> deductMoney(@RequestBody Map<String, Object> request) {
        int accountId = (int) request.get("accountId");
        double amount = (double) request.get("amount");
        bankingService.deductMoney(accountId, amount);
        return ResponseEntity.ok("Money deducted successfully!");
    }
}


/* old code
package com.banking.controller;

import com.banking.model.MoneyRequest;
import com.banking.service.BankingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/balance/{accountId}")
    public double getBalance(@PathVariable int accountId) {
        return bankingService.checkBalance(accountId);
    }
/*
    @PostMapping("/addMoney")
    public String addMoney(@RequestParam int accountId, @RequestParam double amount) {
        bankingService.addMoney(accountId, amount);
        return "Money added successfully!";
    }*//*
    @PostMapping("/addMoney")
    public String addMoney(@RequestBody MoneyRequest moneyRequest) {
        bankingService.addMoney(moneyRequest.getAccountId(), moneyRequest.getAmount());
        return "Money added successfully!";
    }

/*
    @PostMapping("/deductMoney")
    public String deductMoney(@RequestParam int accountId, @RequestParam double amount) {
        bankingService.deductMoney(accountId, amount);
        return "Money deducted successfully!";
    }*/
    /*
    @PostMapping("/deductMoney")
    public String deductMoney(@RequestBody MoneyRequest moneyRequest) {
        bankingService.deductMoney(moneyRequest.getAccountId(), moneyRequest.getAmount());
        return "Money deducted successfully!";
    }

}
*/