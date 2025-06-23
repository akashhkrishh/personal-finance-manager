package in.akashhkrishh.finance;

import in.akashhkrishh.finance.controller.AccountControllerTest;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonalFinanceManagerApplicationTests {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private PersonalFinanceManagerApplication application;

	@BeforeEach
	void setUp() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		application = new PersonalFinanceManagerApplication(userRepository, passwordEncoder);
	}

	@Test
	void init_shouldCreateUser_whenUserDoesNotExist() throws Exception {
		String defaultEmail = "akashhkrishh@gmail.com";
		when(userRepository.existsByEmail(defaultEmail)).thenReturn(false);
		when(passwordEncoder.encode("Pa$$Word!")).thenReturn("encodedPassword");
		application.init().run();
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository, times(1)).save(userCaptor.capture());
		User savedUser = userCaptor.getValue();
		assertEquals("akashhkrishh", savedUser.getUsername());
		assertEquals(defaultEmail, savedUser.getEmail());
		assertEquals("encodedPassword", savedUser.getPassword());
	}

	@Test
	void init_shouldNotCreateUser_whenUserExists() throws Exception {
		String defaultEmail = "akashhkrishh@gmail.com";
		when(userRepository.existsByEmail(defaultEmail)).thenReturn(true);
		application.init().run();
		verify(userRepository, never()).save(any());
	}


}
