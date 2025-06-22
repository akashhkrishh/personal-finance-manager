package in.akashhkrishh.finance.dto;

import in.akashhkrishh.finance.enums.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public record TransactionRequestDTO(

        @NotNull
        @Positive(message = "Amount must be greater than zero")
        Double amount,

        @NotNull(message = "Description is required")
        String description,

        @NotNull(message = "Category must not be null")
        Category category

) implements Serializable {}
