
import java.time.Instant;
import java.util.UUID;

public final class LedgerEntry {

    private final String transactionId;
    private final String transactionType;
    private final Money amount;
    private final String timestamp;

    // Constructor
    public LedgerEntry(String transactionType, Money amount) {

        if (transactionType == null || transactionType.isBlank()) {
            throw new IllegalArgumentException(
                "Transaction type is required"
            );
        }

        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException(
                "Ledger amount must be positive"
            );
        }

        this.transactionId = UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = Instant.now().toString();
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Money getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return transactionType + " | " + amount + " | " + timestamp;
    }
}
