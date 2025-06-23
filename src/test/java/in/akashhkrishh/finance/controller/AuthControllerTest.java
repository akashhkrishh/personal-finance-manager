package in.akashhkrishh.finance.controller;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.dto.LoginRequest;
import in.akashhkrishh.finance.dto.RegisterRequest;
import in.akashhkrishh.finance.dto.TokenResponse;
import in.akashhkrishh.finance.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void login_shouldReturnTokenResponse() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password123");

        TokenResponse tokenResponse = new TokenResponse("fake-jwt-token");
        GlobalResponse<TokenResponse> globalResponse = new GlobalResponse<>(
                tokenResponse,
                "Login successful",
                null,
                true
        );
        ResponseEntity<GlobalResponse<TokenResponse>> responseEntity = ResponseEntity.ok(globalResponse);

        when(authService.login(loginRequest)).thenReturn(responseEntity);

        ResponseEntity<GlobalResponse<TokenResponse>> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        assertEquals("Login successful", response.getBody().message());
        assertEquals("fake-jwt-token", response.getBody().data().token());

        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    void register_shouldReturnTokenResponse() {
        RegisterRequest registerRequest = new RegisterRequest("user", "user@example.com", "password123");

        TokenResponse tokenResponse = new TokenResponse("fake-jwt-token");
        GlobalResponse<TokenResponse> globalResponse = new GlobalResponse<>(
                tokenResponse,
                "User registered successfully",
                null,
                true
        );
        ResponseEntity<GlobalResponse<TokenResponse>> responseEntity = ResponseEntity.status(201).body(globalResponse);

        when(authService.register(registerRequest)).thenReturn(responseEntity);

        ResponseEntity<GlobalResponse<TokenResponse>> response = authController.register(registerRequest);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        assertEquals("User registered successfully", response.getBody().message());
        assertEquals("fake-jwt-token", response.getBody().data().token());

        verify(authService, times(1)).register(registerRequest);
    }
}
