package in.akashhkrishh.finance.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.UserRepository;
import in.akashhkrishh.finance.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JWTFilterTest {

    @Mock
    ApplicationContext applicationContext;

    @Mock
    JWTService jwtService;

    @Mock
    UserRepository userRepository;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    JWTFilter jwtFilter;

    StringWriter responseWriter;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        jwtFilter = new JWTFilter(applicationContext, jwtService);

        when(applicationContext.getBean(UserRepository.class)).thenReturn(userRepository);

        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_noAuthorizationHeader_callsFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals("", responseWriter.toString());
    }

    @Test
    void doFilterInternal_malformedAuthorizationHeader_callsFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals("", responseWriter.toString());
    }

    @Test
    void doFilterInternal_malformedToken_writesUnauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer bad.token.here");
        when(jwtService.extractUserID("bad.token.here")).thenThrow(new RuntimeException("Malformed token"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
        verify(filterChain, never()).doFilter(request, response);

        assertTrue(responseWriter.toString().contains("Malformed or invalid JWT"));
    }

    @Test
    void doFilterInternal_nullUserId_writesUnauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extractUserID("valid.token")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseWriter.toString().contains("Invalid token or user ID not found."));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_userNotFound_writesUnauthorized() throws Exception {
        String userId = UUID.randomUUID().toString();

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extractUserID("valid.token")).thenReturn(userId);
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseWriter.toString().contains("User not found."));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_writesUnauthorized() throws Exception {
        String userId = UUID.randomUUID().toString();
        User user = mock(User.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extractUserID("valid.token")).thenReturn(userId);
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        when(jwtService.validateToken("valid.token", user)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(responseWriter.toString().contains("Invalid token."));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_validToken_setsAuthenticationAndContinues() throws Exception {
        String userId = UUID.randomUUID().toString();
        User user = mock(User.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtService.extractUserID("valid.token")).thenReturn(userId);
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(user));
        when(jwtService.validateToken("valid.token", user)).thenReturn(true);
        when(user.getAuthorities()).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        assertEquals("", responseWriter.toString());
    }
}
