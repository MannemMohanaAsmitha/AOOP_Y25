
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {

    private final String accountNumber;
    private Money balance;
    private final Money overdraftLimit;
    private AccountStatus status;

    private final List<LedgerEntry> ledgerEntries;

    // Constructor
    public BankAccount(
        String accountNumber,
        Money openingBalance,
        Money overdraftLimit
    ) {

        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Account number is required"
            );
        }

        if (openingBalance == null || overdraftLimit == null) {
            throw new IllegalArgumentException(
                "Balance and overdraft limit are required"
            );
        }

        if (!openingBalance.getCurrency().equals(
                overdraftLimit.getCurrency())) {
            throw new IllegalArgumentException(
                "Balance and overdraft limit must use the same currency"
            );
        }

        if (overdraftLimit.isNegative()) {
            throw new IllegalArgumentException(
                "Overdraft limit cannot be negative"
            );
        }

        if (openingBalance.compareTo(overdraftLimit.negate()) < 0) {
            throw new IllegalArgumentException(
                "Opening balance exceeds the overdraft limit"
            );
        }

        this.accountNumber = accountNumber;
        this.balance = openingBalance;
        this.overdraftLimit = overdraftLimit;
        this.status = AccountStatus.ACTIVE;
        this.ledgerEntries = new ArrayList<>();
    }

    // Deposit money
    public synchronized void deposit(Money amount) {

        validateDeposit(amount);

        balance = balance.add(amount);

        ledgerEntries.add(
            new LedgerEntry("DEPOSIT", amount)
        );
    }

    // Withdraw money
    public synchronized void withdraw(Money amount) {

        validateWithdrawal(amount);

        balance = balance.subtract(amount);

        ledgerEntries.add(
            new LedgerEntry("WITHDRAWAL", amount)
        );
    }

    // Check whether deposit is allowed
    synchronized void validateDeposit(Money amount) {

        validatePositiveAmount(amount);
        checkSameCurrency(amount);

        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                "Cannot deposit into a closed account"
            );
        }
    }

    // Check whether withdrawal is allowed
    synchronized void validateWithdrawal(Money amount) {

        validatePositiveAmount(amount);
        checkSameCurrency(amount);

        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                "Only active accounts can withdraw money"
            );
        }

        Money newBalance = balance.subtract(amount);

        if (newBalance.compareTo(overdraftLimit.negate()) < 0) {
            throw new IllegalStateException(
                "Overdraft limit exceeded"
            );
        }
    }

    // Validate transaction amount
    private void validatePositiveAmount(Money amount) {

        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException(
                "Transaction amount must be greater than zero"
            );
        }
    }

    // Check currency
    private void checkSameCurrency(Money amount) {

        if (!balance.getCurrency().equals(amount.getCurrency())) {
            throw new IllegalArgumentException(
                "Transaction currency does not match account currency"
            );
        }
    }

    // Freeze account
    public synchronized void freeze() {

        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                "Only an active account can be frozen"
            );
        }

        status = AccountStatus.FROZEN;
    }

    // Unfreeze account
    public synchronized void unfreeze() {

        if (status != AccountStatus.FROZEN) {
            throw new IllegalStateException(
                "Only a frozen account can be unfrozen"
            );
        }

        status = AccountStatus.ACTIVE;
    }

    // Close account
    public synchronized void close() {

        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                "Account is already closed"
            );
        }

        if (!balance.isZero()) {
            throw new IllegalStateException(
                "Account balance must be zero before closing"
            );
        }

        status = AccountStatus.CLOSED;
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public synchronized Money getBalance() {
        return balance;
    }

    public synchronized AccountStatus getStatus() {
        return status;
    }

    public Money getOverdraftLimit() {
        return overdraftLimit;
    }

    public synchronized List<LedgerEntry> getLedgerEntries() {

        return Collections.unmodifiableList(
            new ArrayList<>(ledgerEntries)
        );
    }
}
