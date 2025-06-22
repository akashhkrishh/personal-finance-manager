package in.akashhkrishh.finance.dto;

public record GlobalResponse<T>(
        T data,
        String message,
        String error,
        boolean success
){ }
