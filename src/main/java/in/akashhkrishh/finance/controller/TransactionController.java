package in.akashhkrishh.finance.controller;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.dto.TransactionRequestDTO;
import in.akashhkrishh.finance.model.Transaction;
import in.akashhkrishh.finance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public ResponseEntity<GlobalResponse<Transaction>> getSingleTransaction(@RequestParam UUID id) {
        return transactionService.getSingleTransaction(id);
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalResponse<List<Transaction>>> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Transaction>> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return transactionService.createTransaction(transactionRequestDTO);
    }

    @PutMapping("/")
    public ResponseEntity<GlobalResponse<Transaction>> updateTransaction(@RequestParam UUID id, @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return transactionService.updateTransaction(id, transactionRequestDTO);
    }

    @DeleteMapping("/")
    public ResponseEntity<GlobalResponse<Transaction>> deleteTransaction(@RequestParam UUID id) {
        return transactionService.deleteTransaction(id);
    }
}
