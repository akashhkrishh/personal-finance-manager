package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.dto.TransactionRequestDTO;
import in.akashhkrishh.finance.enums.Category;
import in.akashhkrishh.finance.exception.TransactionNotFoundException;
import in.akashhkrishh.finance.exception.UserNotAuthenticatedException;
import in.akashhkrishh.finance.model.Transaction;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionService transactionService;
    private User mockUser;
    private Transaction mockTransaction;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
        mockUser = new User(UUID.randomUUID(),"User","user@example.com", "encode-password");
        mockTransaction = new Transaction(
                UUID.randomUUID(),
                mockUser,
                100.0,
                Category.EXPENSE,
                "Groceries",
                Instant.now()
        );
    }

    @Test
    void getTransactionById_Success() {
        when(transactionRepository.findById(mockTransaction.getId())).thenReturn(Optional.of(mockTransaction));

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(mockUser);

            Transaction result = transactionService.getTransactionById(mockTransaction.getId());
            assertEquals(mockTransaction, result);
        }
    }

    @Test
    void getTransactionById_NotFound() {
        when(transactionRepository.findById(mockTransaction.getId())).thenReturn(Optional.empty());

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(mockUser);

            assertThrows(TransactionNotFoundException.class,
                    () -> transactionService.getTransactionById(mockTransaction.getId()));
        }
    }

    @Test
    void getTransactionById_UserNotAuthenticated() {
        when(transactionRepository.findById(mockTransaction.getId())).thenReturn(Optional.of(mockTransaction));

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(null);

            assertThrows(UserNotAuthenticatedException.class,
                    () -> transactionService.getTransactionById(mockTransaction.getId()));
        }
    }

    @Test
    void getTransactionById_TransactionDoesNotBelongToUser() {
        when(transactionRepository.findById(mockTransaction.getId())).thenReturn(Optional.of(mockTransaction));

        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(otherUser);

            assertThrows(TransactionNotFoundException.class,
                    () -> transactionService.getTransactionById(mockTransaction.getId()));
        }
    }

    @Test
    void getSingleTransaction_Success() {
        when(transactionRepository.findById(mockTransaction.getId())).thenReturn(Optional.of(mockTransaction));

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(mockUser);

            ResponseEntity<GlobalResponse<Transaction>> response = transactionService.getSingleTransaction(mockTransaction.getId());

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(mockTransaction, response.getBody().data());
            assertTrue(response.getBody().success());
        }
    }

    @Test
    void getAllTransactions_Success() {
        List<Transaction> transactions = List.of(mockTransaction);
        when(transactionRepository.findByUser(mockUser)).thenReturn(transactions);

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(mockUser);

            ResponseEntity<GlobalResponse<List<Transaction>>> response = transactionService.getAllTransactions();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(transactions, response.getBody().data());
            assertTrue(response.getBody().success());
        }
    }

    @Test
    void getAllTransactions_UserNotAuthenticated() {
        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(null);

            assertThrows(UserNotAuthenticatedException.class, () -> transactionService.getAllTransactions());
        }
    }

    @Test
    void createTransaction_Success() {
        TransactionRequestDTO dto = new TransactionRequestDTO(200.0, "Salary", Category.INCOME);

        Transaction savedTransaction = new Transaction();
        BeanUtils.copyProperties(dto, savedTransaction);
        savedTransaction.setUser(mockUser);
        savedTransaction.setId(UUID.randomUUID());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(mockUser);

            ResponseEntity<GlobalResponse<Transaction>> response = transactionService.createTransaction(dto);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(savedTransaction, response.getBody().data());
            assertTrue(response.getBody().success());
        }
    }

    @Test
    void createTransaction_UserNotAuthenticated() {
        TransactionRequestDTO dto = new TransactionRequestDTO(200.0, "Salary", Category.INCOME);

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(null);

            assertThrows(UserNotAuthenticatedException.class, () -> transactionService.createTransaction(dto));
        }
    }

    @Test
    void updateTransaction_Success() {
        TransactionRequestDTO dto = new TransactionRequestDTO(150.0, "Updated Desc", Category.EXPENSE);

        when(transactionRepository.findById(mockTransaction.getId())).thenReturn(Optional.of(mockTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(mockUser);

            ResponseEntity<GlobalResponse<Transaction>> response = transactionService.updateTransaction(mockTransaction.getId(), dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(dto.amount(), response.getBody().data().getAmount());
            assertEquals(dto.description(), response.getBody().data().getDescription());
            assertEquals(dto.category(), response.getBody().data().getCategory());
            assertTrue(response.getBody().success());
        }
    }

    @Test
    void updateTransaction_UserNotAuthenticated() {
        TransactionRequestDTO dto = new TransactionRequestDTO(150.0, "Updated Desc", Category.EXPENSE);
        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(null);
            assertThrows(UserNotAuthenticatedException.class, () -> transactionService.updateTransaction(UUID.randomUUID(), dto));
        }
    }

    @Test
    void deleteTransaction_Success() {
        when(transactionRepository.findById(mockTransaction.getId())).thenReturn(Optional.of(mockTransaction));
        doNothing().when(transactionRepository).deleteById(mockTransaction.getId());

        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(mockUser);

            ResponseEntity<GlobalResponse<Transaction>> response = transactionService.deleteTransaction(mockTransaction.getId());

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(mockTransaction, response.getBody().data());
            assertTrue(response.getBody().success());
        }
    }

    @Test
    void deleteTransaction_UserNotAuthenticated() {
        try (MockedStatic<AuthService> authServiceMock = Mockito.mockStatic(AuthService.class)) {
            authServiceMock.when(AuthService::getCurrentUser).thenReturn(null);
            assertThrows(UserNotAuthenticatedException.class, () -> transactionService.deleteTransaction(UUID.randomUUID()));
        }
    }
}
