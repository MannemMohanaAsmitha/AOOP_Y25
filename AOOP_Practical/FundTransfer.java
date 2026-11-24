
public class FundTransfer {

    private final String transferId;
    private final BankAccount source;
    private final BankAccount destination;
    private final Money amount;

    // Constructor
    public FundTransfer(
        String transferId,
        BankAccount source,
        BankAccount destination,
        Money amount
    ) {

        if (transferId == null || transferId.isBlank()) {
            throw new IllegalArgumentException(
                "Transfer ID is required"
            );
        }

        if (source == null || destination == null || amount == null) {
            throw new IllegalArgumentException(
                "Source, destination and amount are required"
            );
        }

        if (source == destination) {
            throw new IllegalArgumentException(
                "Source and destination must be different accounts"
            );
        }

        if (!amount.isPositive()) {
            throw new IllegalArgumentException(
                "Transfer amount must be greater than zero"
            );
        }

        if (!source.getBalance().getCurrency().equals(
                amount.getCurrency())
            || !destination.getBalance().getCurrency().equals(
                amount.getCurrency())) {

            throw new IllegalArgumentException(
                "Transfer currency must match both accounts"
            );
        }

        this.transferId = transferId;
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    // Transfer money
    public void transfer() {

        // Validate both accounts before deducting money
        source.validateWithdrawal(amount);
        destination.validateDeposit(amount);

        // Deduct money from source
        source.withdraw(amount);

        try {

            // Add money to destination
            destination.deposit(amount);

            System.out.println(
                "Transfer " + transferId +
                " completed successfully."
            );

        } catch (RuntimeException error) {

            // Refund the source account if destination credit fails
            source.deposit(amount);

            throw new IllegalStateException(
                "Transfer failed. Source account was refunded.",
                error
            );
        }
    }
}
