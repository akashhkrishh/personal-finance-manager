package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.dto.*;
import in.akashhkrishh.finance.exception.InvalidCredentialsException;
import in.akashhkrishh.finance.exception.UserAlreadyExistsException;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthService authService;

    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    private User testUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail(testEmail);
        testUser.setPassword("encoded-password");
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        when(userRepository.findByEmail(testEmail.toLowerCase())).thenReturn(List.of(testUser));
        when(passwordEncoder.matches(testPassword, testUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(testUser.getId())).thenReturn("mock-token");

        ResponseEntity<GlobalResponse<TokenResponse>> response = authService.login(loginRequest);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("mock-token", response.getBody().data().token());
        assertTrue(response.getBody().success());
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("nouser@example.com", "irrelevant");
        when(userRepository.findByEmail("nouser@example.com")).thenReturn(Collections.emptyList());
        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testLoginInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest(testEmail, "wrong_pass");

        when(userRepository.findByEmail(testEmail.toLowerCase())).thenReturn(List.of(testUser));
        when(passwordEncoder.matches("wrong_pass", testUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("User", testEmail, testPassword);

        when(userRepository.existsByEmail(testEmail)).thenReturn(false);
        when(passwordEncoder.encode(testPassword)).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(UUID.class))).thenReturn("new-token");

        ResponseEntity<GlobalResponse<TokenResponse>> response = authService.register(registerRequest);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("new-token", response.getBody().data().token());
        assertTrue(response.getBody().success());
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest("Test User", testEmail, testPassword);
        when(userRepository.existsByEmail(testEmail)).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequest));
    }
}
