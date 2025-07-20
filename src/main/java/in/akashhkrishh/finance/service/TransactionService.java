package in.akashhkrishh.finance.service;

import in.akashhkrishh.finance.dto.GlobalResponse;
import in.akashhkrishh.finance.dto.TransactionRequestDTO;
import in.akashhkrishh.finance.exception.TransactionNotFoundException;
import in.akashhkrishh.finance.exception.UserNotAuthenticatedException;
import in.akashhkrishh.finance.model.Transaction;
import in.akashhkrishh.finance.model.User;
import in.akashhkrishh.finance.repository.TransactionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static in.akashhkrishh.finance.service.AuthService.getCurrentUser;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found for ID: " + id));
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new UserNotAuthenticatedException("Current user is not authenticated.");
        }
        if (!transaction.getUser().getId().equals(currentUser.getId())) {
            throw new TransactionNotFoundException("Transaction ID: " + id + " does not belong to the current user.");
        }
        return transaction;
    }

    public ResponseEntity<GlobalResponse<Transaction>> getSingleTransaction(UUID id) {
        Transaction transaction = getTransactionById(id);
        GlobalResponse<Transaction> response = new GlobalResponse<>(
                transaction,
                "Transaction fetched successfully",
                null,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<GlobalResponse<List<Transaction>>> getAllTransactions() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new UserNotAuthenticatedException("Current user is not authenticated.");
        }
        List<Transaction> transactions = transactionRepository.findByUser(currentUser);

        GlobalResponse<List<Transaction>> response = new GlobalResponse<>(
                transactions,
                "Transactions fetched successfully",
                null,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<GlobalResponse<Transaction>> createTransaction(TransactionRequestDTO transactionRequestDTO) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new UserNotAuthenticatedException("Current user is not authenticated.");
        }
        Transaction transaction = new Transaction();
        transaction.setUser(currentUser);
        BeanUtils.copyProperties(transactionRequestDTO, transaction);
        Transaction savedTransaction = transactionRepository.save(transaction);
        GlobalResponse<Transaction> response = new GlobalResponse<>(
                savedTransaction,
                "Transaction Created Successfully!",
                null,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }

    public ResponseEntity<GlobalResponse<Transaction>> updateTransaction(UUID id, TransactionRequestDTO transactionRequestDTO) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new UserNotAuthenticatedException("Current user is not authenticated.");
        }
        Transaction transaction = getTransactionById(id);
        transaction.setAmount(transactionRequestDTO.amount());
        transaction.setDescription(transactionRequestDTO.description());
        transaction.setCategory(transactionRequestDTO.category());
        Transaction updatededTransaction = transactionRepository.save(transaction);
        GlobalResponse<Transaction> response = new GlobalResponse<>(
                 updatededTransaction,
                "Transaction update successfully",
                null,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<GlobalResponse<Transaction>> deleteTransaction(UUID id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new UserNotAuthenticatedException("Current user is not authenticated.");
        }
        Transaction transaction = getTransactionById(id);
        transactionRepository.deleteById(id);
        GlobalResponse<Transaction> response = new GlobalResponse<>(
                transaction,
                "Transaction deleted successfully",
                null,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
