package in.akashhkrishh.finance.controller;

import in.akashhkrishh.finance.dto.AccountResponse;
import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/getBalance")
    public ResponseEntity<GlobalResponse<AccountResponse>> getBalance() {
        return accountService.getAccountBalance();
    }
}
