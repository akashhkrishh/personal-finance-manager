package in.akashhkrishh.finance.repository;

import in.akashhkrishh.finance.model.Transaction;
import in.akashhkrishh.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUser(User first);
}
