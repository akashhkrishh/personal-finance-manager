package in.akashhkrishh.finance;

import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PersonalFinanceManagerApplication {

	private static final Logger logger = LoggerFactory.getLogger(PersonalFinanceManagerApplication.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public PersonalFinanceManagerApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(PersonalFinanceManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			String defaultEmail = "akashhkrishh@gmail.com";
			if (!userRepository.existsByEmail(defaultEmail)) {
				User newUser = new User();
				newUser.setUsername("akashhkrishh");
				newUser.setEmail(defaultEmail);
				newUser.setPassword(passwordEncoder.encode("Pa$$Word!"));
				userRepository.save(newUser);
				logger.info("Created default user 'akashhkrishh'");
			} else {
				logger.info("Default user 'akashhkrishh' already exists");
			}
		};
	}
}
