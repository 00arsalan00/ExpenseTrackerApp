package com.app.entity;

import java.math.BigDecimal;
import java.time.*;
import com.app.domain.ExpenseType;
import com.app.domain.ExpenseCategory;
import jakarta.persistence.*;

@Entity
@Table(
    name = "expenses",
    indexes = {
        @Index(name = "idx_user_date", columnList = "user_id, expense_date")
    }
)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseType type;

    @Column(length = 255)
    private String description;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public void setUserId(Long userId) {
        if (this.userId != null) {
            throw new IllegalStateException("userId cannot be changed");
        }
        this.userId = userId;
    }
}
