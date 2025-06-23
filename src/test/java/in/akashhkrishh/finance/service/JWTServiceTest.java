package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.model.User;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JWTServiceTest {

    private JWTService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();

        String base64Secret = "bXktc2VjdXJlLWtleS0xMjM0NTY3ODkwMTIzNDU2";
        ReflectionTestUtils.setField(jwtService, "secret", base64Secret);

        user = new User();
        user.setId(UUID.randomUUID());
    }

    @Test
    void testGenerateAndExtractUserId() {
        String token = jwtService.generateToken(user.getId());
        assertNotNull(token);

        String extractedId = jwtService.extractUserID(token);
        assertEquals(user.getId().toString(), extractedId);
    }

    @Test
    void testValidateToken_Success() {
        String token = jwtService.generateToken(user.getId());
        boolean isValid = jwtService.validateToken(token, user);
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUser() {
        String token = jwtService.generateToken(UUID.randomUUID());
        boolean isValid = jwtService.validateToken(token, user);
        assertFalse(isValid);
    }

    @Test
    void testTokenExpiration() throws Exception {
        Method method = jwtService.getClass().getDeclaredMethod("getSigningKey");
        method.setAccessible(true);
        SecretKey key = (SecretKey) method.invoke(jwtService);

        String token = Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new java.util.Date(System.currentTimeMillis()))
                .expiration(new java.util.Date(System.currentTimeMillis() + 1))
                .signWith(key)
                .compact();

        Thread.sleep(5);
        boolean isValid = jwtService.validateToken(token, user);
        assertFalse(isValid);
    }
}
