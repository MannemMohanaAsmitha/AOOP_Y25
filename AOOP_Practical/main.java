public class main {

    public static void main(String[] args) {

        // Create opening balances
        Money openingBalanceA = Money.ofRupees(10000);
        Money openingBalanceB = Money.ofRupees(3000);

        Money overdraftLimit = Money.ofRupees(2000);

        // Create bank accounts
        BankAccount accountA = new BankAccount(
            "ACC101",
            openingBalanceA,
            overdraftLimit
        );

        BankAccount accountB = new BankAccount(
            "ACC102",
            openingBalanceB,
            overdraftLimit
        );

        System.out.println("Before transfer:");

        System.out.println(
            "Account A: " + accountA.getBalance()
        );

        System.out.println(
            "Account B: " + accountB.getBalance()
        );

        // Create and perform money transfer
        FundTransfer transfer = new FundTransfer(
            "TRF1001",
            accountA,
            accountB,
            Money.ofRupees(5000)
        );

        transfer.transfer();

        System.out.println("\nAfter transfer:");

        System.out.println(
            "Account A: " + accountA.getBalance()
        );

        System.out.println(
            "Account B: " + accountB.getBalance()
        );

        // Deposit money
        accountA.deposit(Money.ofRupees(1000));

        // Withdraw money
        accountB.withdraw(Money.ofRupees(500));

        // Freeze account
        accountA.freeze();

        System.out.println(
            "\nAccount A status: " + accountA.getStatus()
        );

        // Try withdrawal from frozen account
        try {

            accountA.withdraw(Money.ofRupees(100));

        } catch (IllegalStateException error) {

            System.out.println(
                "Withdrawal rejected: " + error.getMessage()
            );
        }

        // Unfreeze account
        accountA.unfreeze();

        System.out.println(
            "Account A status after unfreezing: "
            + accountA.getStatus()
        );

        // Display transaction history
        System.out.println("\nAccount A transaction history:");

        for (LedgerEntry entry : accountA.getLedgerEntries()) {

            System.out.println(entry);
        }

        // Close an empty account
        BankAccount emptyAccount = new BankAccount(
            "ACC103",
            Money.ofRupees(0),
            Money.ofRupees(0)
        );

        emptyAccount.close();

        System.out.println(
            "\nEmpty account status after closing: "
            + emptyAccount.getStatus()
        );
    }
}