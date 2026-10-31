package org.example.bankaccountweb;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class BankAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BankAccount findByAccountNumber(long accountNumber) {
        String sql = "SELECT * FROM bank_accounts WHERE account_number = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new BankAccount(
                                rs.getLong("account_number"),
                                rs.getString("account_holder"),
                                rs.getBigDecimal("balance")
                        ),
                accountNumber
        );
    }

    public boolean existsByAccountNumber(long accountNumber) {
        String sql = "SELECT COUNT(*) FROM bank_accounts WHERE account_number = ?";

        Integer count = jdbcTemplate.queryForObject(
                sql, Integer.class, accountNumber);

        return count != null && count > 0;
    }

    public void save(BankAccount account) {
        String sql = "INSERT INTO bank_accounts " +
                "(account_number, account_holder, balance) VALUES (?, ?, ?)";

        jdbcTemplate.update(
                sql,
                account.getAccountNumber(),
                account.getAccountHolder(),
                account.getBalance()
        );
    }

    public int deposit(long accountNumber, BigDecimal amount) {
        String sql = "UPDATE bank_accounts SET balance = balance + ? " +
                "WHERE account_number = ?";

        return jdbcTemplate.update(sql, amount, accountNumber);
    }

    public int withdraw(long accountNumber, BigDecimal amount) {
        String sql = "UPDATE bank_accounts SET balance = balance - ? " +
                "WHERE account_number = ? AND balance >= ?";

        return jdbcTemplate.update(sql, amount, accountNumber, amount);
    }

    public void saveTransaction(long accountNumber, String type, BigDecimal amount) {
        String sql = "INSERT INTO bank_transactions " +
                "(account_number, transaction_type, amount) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, accountNumber, type, amount);
    }

    public List<BankTransaction> findTransactionsByAccountNumber(long accountNumber) {
        String sql = "SELECT * FROM bank_transactions " +
                "WHERE account_number = ? ORDER BY transaction_date DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new BankTransaction(
                                rs.getLong("transaction_id"),
                                rs.getLong("account_number"),
                                rs.getString("transaction_type"),
                                rs.getBigDecimal("amount"),
                                rs.getTimestamp("transaction_date").toLocalDateTime()
                        ),
                accountNumber
        );
    }
}