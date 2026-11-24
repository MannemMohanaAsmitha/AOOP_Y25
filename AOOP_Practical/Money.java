
import java.math.BigDecimal;

public final class Money {

    private final long amountInPaise;
    private final String currency;

    // Constructor
    public Money(long amountInPaise, String currency) {

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        this.amountInPaise = amountInPaise;
        this.currency = currency.trim().toUpperCase();
    }

    // Convert rupees into paise
    public static Money ofRupees(long rupees) {
        return new Money(Math.multiplyExact(rupees, 100L), "INR");
    }

    public long getAmountInPaise() {
        return amountInPaise;
    }

    public String getCurrency() {
        return currency;
    }

    // Check whether amount is positive
    public boolean isPositive() {
        return amountInPaise > 0;
    }

    public boolean isNegative() {
        return amountInPaise < 0;
    }

    public boolean isZero() {
        return amountInPaise == 0;
    }

    // Addition
    public Money add(Money other) {
        checkCurrency(other);

        return new Money(
            Math.addExact(amountInPaise, other.amountInPaise),
            currency
        );
    }

    // Subtraction
    public Money subtract(Money other) {
        checkCurrency(other);

        return new Money(
            Math.subtractExact(amountInPaise, other.amountInPaise),
            currency
        );
    }

    public Money negate() {
        return new Money(Math.negateExact(amountInPaise), currency);
    }

    public int compareTo(Money other) {
        checkCurrency(other);
        return Long.compare(amountInPaise, other.amountInPaise);
    }

    // Check whether both amounts use the same currency
    private void checkCurrency(Money other) {
        if (other == null || !currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Money values must use the same currency"
            );
        }
    }

    @Override
    public String toString() {
        String value = BigDecimal.valueOf(
            amountInPaise, 2
        ).toPlainString();

        return currency + " " + value;
    }
}
