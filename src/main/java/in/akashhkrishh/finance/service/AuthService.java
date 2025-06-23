package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.dto.LoginRequest;
import in.akashhkrishh.finance.dto.RegisterRequest;
import in.akashhkrishh.finance.dto.TokenResponse;
import in.akashhkrishh.finance.exception.InvalidCredentialsException;
import in.akashhkrishh.finance.exception.UserAlreadyExistsException;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public ResponseEntity<GlobalResponse<TokenResponse>> login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().toLowerCase();
        String rawPassword = loginRequest.getPassword();

        List<User> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User with email " + email + " not found.");
        }

        User user = users.getFirst();
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        String token = jwtService.generateToken(user.getId());
        TokenResponse tokenResponse = new TokenResponse(token);

        GlobalResponse<TokenResponse> response = new GlobalResponse<>(
                tokenResponse,
                "Login successful",
                null,
                true
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<GlobalResponse<TokenResponse>> register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getId());

        TokenResponse tokenResponse = new TokenResponse(token);
        GlobalResponse<TokenResponse> response = new GlobalResponse<>(
                tokenResponse,
                "User registered successfully",
                null,
                true
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                return user;
            }
        }
        return null;
    }

    public static boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
}
