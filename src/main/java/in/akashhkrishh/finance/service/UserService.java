package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public ResponseEntity<GlobalResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        GlobalResponse<List<User>> globalResponse = new GlobalResponse<>(
                users,
                "All User fetched successfully",
                "",
                true
        );
        return new ResponseEntity<>(globalResponse, HttpStatus.OK);
    }

    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email);
    }

}
