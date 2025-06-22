package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.dto.AccountResponse;
import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.enums.Category;
import in.akashhkrishh.finance.model.Transaction;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static in.akashhkrishh.finance.service.AuthService.getCurrentUser;

@Service
public class AccountService {

    private final TransactionRepository transactionRepository;

    public AccountService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public ResponseEntity<GlobalResponse<AccountResponse>> getAccountBalance() {
        User currentUser = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUser(currentUser);

        AccountResponse accountResponse = calculateBalance(transactions);
        GlobalResponse<AccountResponse> response = new GlobalResponse<>(
                accountResponse,
                "Balance calculated successfully",
                null,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private AccountResponse calculateBalance(List<Transaction> transactions) {
        double balance = 0.0;

        for (Transaction tx : transactions) {
            if (tx.getCategory() == Category.INCOME) {
                balance += tx.getAmount();
            } else {
                balance -= tx.getAmount();
            }
        }

        double rounded = Math.round(balance * 100.0) / 100.0;
        return new AccountResponse(rounded);
    }

}
