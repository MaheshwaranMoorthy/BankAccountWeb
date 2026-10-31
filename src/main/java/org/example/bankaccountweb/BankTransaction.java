package org.example.bankaccountweb;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransaction {

    private long transactionId;
    private long accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    public BankTransaction(long transactionId, long accountNumber,
                           String transactionType, BigDecimal amount,
                           LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}