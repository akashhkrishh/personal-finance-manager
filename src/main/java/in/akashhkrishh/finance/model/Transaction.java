package in.akashhkrishh.finance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.akashhkrishh.finance.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Positive(message = "Amount must be greater than zero")
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Category must not be null")
    private Category category;

    @Column(nullable = false)
    @NotNull(message = "Description is required")
    private String description;

    @CreationTimestamp
    private Instant timestamp;
}
