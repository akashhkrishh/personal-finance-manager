package in.akashhkrishh.finance.controller;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.dto.LoginRequest;
import in.akashhkrishh.finance.dto.RegisterRequest;
import in.akashhkrishh.finance.dto.TokenResponse;
import in.akashhkrishh.finance.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<GlobalResponse<TokenResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

//    @GetMapping("/info")
//    public ResponseEntity<?> getInfo() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth.getDetails() instanceof WebAuthenticationDetails details) {
//            String ip = details.getRemoteAddress();
//            String sessionId = details.getSessionId();
//            return ResponseEntity.ok("Logged in from IP: " + ip + ", session: " + sessionId);
//        }
//
//        return ResponseEntity.ok("No details available");
//    }


}

