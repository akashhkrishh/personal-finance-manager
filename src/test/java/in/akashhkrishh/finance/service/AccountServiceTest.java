package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.dto.AccountResponse;
import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.enums.Category;
import in.akashhkrishh.finance.exception.UserNotAuthenticatedException;
import in.akashhkrishh.finance.model.Transaction;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setEmail("john@example.com");
    }

    @Test
    void testGetAccountBalance() {

        try (MockedStatic<AuthService> mockedAuthService = mockStatic(AuthService.class)) {
            mockedAuthService.when(AuthService::getCurrentUser).thenReturn(mockUser);


            List<Transaction> mockTransactions = Arrays.asList(
                    new Transaction(null, mockUser, 1000.0, Category.INCOME, "Salary", null),
                    new Transaction(null, mockUser, 300.0, Category.EXPENSE, "Groceries", null)
            );

            when(transactionRepository.findByUser(mockUser)).thenReturn(mockTransactions);

            ResponseEntity<GlobalResponse<AccountResponse>> response = accountService.getAccountBalance();

            assertNotNull(response.getBody());
            assertEquals(200, response.getStatusCode().value());
            assertTrue(response.getBody().success());
            assertEquals(700.0, response.getBody().data().balance());
        }
    }
    @Test
    void testGetAccountBalance_UnauthenticatedUser_ThrowsException() {
        try (MockedStatic<AuthService> mockedAuthService = mockStatic(AuthService.class)) {
            mockedAuthService.when(AuthService::getCurrentUser).thenReturn(null);

            UserNotAuthenticatedException exception = assertThrows(
                    UserNotAuthenticatedException.class,
                    () -> accountService.getAccountBalance()
            );

            assertEquals("User is not authenticated", exception.getMessage());
        }
    }

}
