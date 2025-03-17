package com.banking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Change to int

    @Column(name = "account_holder", nullable = false)
    private String accountHolder;

    @Column(name = "balance", nullable = false)
    private double balance;

    public Account() {}

    public Account(int id, String accountHolder, double balance) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public int getId() { return id; }  // Change to int
    public void setId(int id) { this.id = id; }  // Change to int

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
