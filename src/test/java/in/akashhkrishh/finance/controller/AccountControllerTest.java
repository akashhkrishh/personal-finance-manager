package in.akashhkrishh.finance.controller;

import in.akashhkrishh.finance.dto.AccountResponse;
import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    private AccountService accountService;
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        accountController = new AccountController(accountService);
    }

    @Test
    void getBalance_shouldReturnAccountBalanceResponse() {
        AccountResponse accountResponse = new AccountResponse(1500.0);
        GlobalResponse<AccountResponse> globalResponse = new GlobalResponse<>(
                accountResponse,
                "Balance calculated successfully",
                null,
                true
        );

        ResponseEntity<GlobalResponse<AccountResponse>> responseEntity = ResponseEntity.ok(globalResponse);

        when(accountService.getAccountBalance()).thenReturn(responseEntity);

        ResponseEntity<GlobalResponse<AccountResponse>> response = accountController.getBalance();
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        assertEquals(1500.0, response.getBody().data().balance());
        assertEquals("Balance calculated successfully", response.getBody().message());

        verify(accountService, times(1)).getAccountBalance();
    }
}
