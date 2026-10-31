package org.example.bankaccountweb;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountRepository repository;

    public BankAccountController(BankAccountRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/{accountNumber}")
    public BankAccount getAccount(@PathVariable long accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }
    @PostMapping
    public String createAccount(@RequestBody BankAccount account) {

        if (account.getAccountHolder() == null ||
                account.getAccountHolder().trim().isEmpty()) {
            return "Account holder name is required.";
        }

        if (account.getBalance() == null ||
                account.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            return "Opening balance must be greater than zero.";
        }

        if (repository.existsByAccountNumber(account.getAccountNumber())) {
            return "Account number already exists.";
        }

        repository.save(account);

        return "Account created successfully";
    }
    @PostMapping("/{accountNumber}/deposit")
    public String deposit(
            @PathVariable long accountNumber,
            @RequestParam BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "Deposit amount must be greater than zero.";
        }

        int rowsUpdated = repository.deposit(accountNumber, amount);

        if (rowsUpdated == 0) {
            return "Deposit failed: Account not found.";
        }
        repository.saveTransaction(accountNumber, "DEPOSIT", amount);
        return "Deposit successful";
    }
    @PostMapping("/{accountNumber}/withdraw")
    public String withdraw(
            @PathVariable long accountNumber,
            @RequestParam BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return "Withdrawal amount must be greater than zero.";
        }

        int rowsUpdated = repository.withdraw(accountNumber, amount);

        if (rowsUpdated == 0) {
            return "Withdrawal failed: Account not found or insufficient balance.";
        }
        repository.saveTransaction(accountNumber, "WITHDRAW", amount);
        return "Withdrawal successful";
    }
    @GetMapping("/{accountNumber}/transactions")
    public List<BankTransaction> getTransactions(
            @PathVariable long accountNumber) {

        return repository.findTransactionsByAccountNumber(accountNumber);
    }
}