package com.example.seminar.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    protected Account() {
    }

    public Account(String owner, BigDecimal amount) {
        this.owner = owner;
        this.amount = amount;
    }

    public Account(Long id, String owner, BigDecimal amount) {
        this.id = id;
        this.owner = owner;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void changeAmount(BigDecimal amount) {
        this.amount = amount;
    }
}