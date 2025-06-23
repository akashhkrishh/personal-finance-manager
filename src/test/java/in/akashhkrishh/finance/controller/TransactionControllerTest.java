package in.akashhkrishh.finance.controller;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.dto.TransactionRequestDTO;
import in.akashhkrishh.finance.enums.Category;
import in.akashhkrishh.finance.model.Transaction;
import in.akashhkrishh.finance.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    private TransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        transactionController = new TransactionController(transactionService);
    }

    @Test
    void getSingleTransaction_returnsTransaction() {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction();
        GlobalResponse<Transaction> response = new GlobalResponse<>(transaction, "Fetched", null, true);
        ResponseEntity<GlobalResponse<Transaction>> entity = ResponseEntity.ok(response);

        when(transactionService.getSingleTransaction(id)).thenReturn(entity);

        ResponseEntity<GlobalResponse<Transaction>> result = transactionController.getSingleTransaction(id);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().success());
        assertEquals("Fetched", result.getBody().message());
        assertSame(transaction, result.getBody().data());

        verify(transactionService, times(1)).getSingleTransaction(id);
    }

    @Test
    void getAllTransactions_returnsList() {
        List<Transaction> transactions = List.of(new Transaction(), new Transaction());
        GlobalResponse<List<Transaction>> response = new GlobalResponse<>(transactions, "All fetched", null, true);
        ResponseEntity<GlobalResponse<List<Transaction>>> entity = ResponseEntity.ok(response);

        when(transactionService.getAllTransactions()).thenReturn(entity);

        ResponseEntity<GlobalResponse<List<Transaction>>> result = transactionController.getAllTransactions();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().success());
        assertEquals("All fetched", result.getBody().message());
        assertSame(transactions, result.getBody().data());

        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    void createTransaction_returnsCreatedTransaction() {
        TransactionRequestDTO dto = new TransactionRequestDTO(100.0, "desc", Category.INCOME);
        Transaction createdTransaction = new Transaction();
        GlobalResponse<Transaction> response = new GlobalResponse<>(createdTransaction, "Created", null, true);
        ResponseEntity<GlobalResponse<Transaction>> entity = ResponseEntity.status(201).body(response);

        when(transactionService.createTransaction(dto)).thenReturn(entity);

        ResponseEntity<GlobalResponse<Transaction>> result = transactionController.createTransaction(dto);

        assertEquals(201, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().success());
        assertEquals("Created", result.getBody().message());
        assertSame(createdTransaction, result.getBody().data());

        verify(transactionService, times(1)).createTransaction(dto);
    }

    @Test
    void updateTransaction_returnsUpdatedTransaction() {

        UUID id = UUID.randomUUID();
        TransactionRequestDTO dto = new TransactionRequestDTO(200.0, "updated desc", Category.INCOME);
        Transaction updatedTransaction = new Transaction();
        GlobalResponse<Transaction> response = new GlobalResponse<>(updatedTransaction, "Updated", null, true);
        ResponseEntity<GlobalResponse<Transaction>> entity = ResponseEntity.ok(response);

        when(transactionService.updateTransaction(id, dto)).thenReturn(entity);

        ResponseEntity<GlobalResponse<Transaction>> result = transactionController.updateTransaction(id, dto);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().success());
        assertEquals("Updated", result.getBody().message());
        assertSame(updatedTransaction, result.getBody().data());

        verify(transactionService, times(1)).updateTransaction(id, dto);
    }

    @Test
    void deleteTransaction_returnsDeletedTransaction() {
        UUID id = UUID.randomUUID();
        Transaction deletedTransaction = new Transaction();
        GlobalResponse<Transaction> response = new GlobalResponse<>(deletedTransaction, "Deleted", null, true);
        ResponseEntity<GlobalResponse<Transaction>> entity = ResponseEntity.ok(response);

        when(transactionService.deleteTransaction(id)).thenReturn(entity);

        ResponseEntity<GlobalResponse<Transaction>> result = transactionController.deleteTransaction(id);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().success());
        assertEquals("Deleted", result.getBody().message());
        assertSame(deletedTransaction, result.getBody().data());

        verify(transactionService, times(1)).deleteTransaction(id);
    }
}
