package in.akashhkrishh.finance.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    INCOME,
    EXPENSE;

    @JsonCreator
    public static Category fromString(String value) {
        try {
            return Category.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid category: " + value);
        }
    }
}
