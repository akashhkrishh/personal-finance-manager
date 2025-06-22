package in.akashhkrishh.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotEmpty
    @Email
    public String email;

    @NotEmpty
    public String password;

}
