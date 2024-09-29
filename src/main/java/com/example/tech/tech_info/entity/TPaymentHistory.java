package com.example.tech.tech_info.entity;

import java.time.LocalDate;

public class TPaymentHistory {

    private Long id;
    private Long customerId;
    private String comment;
    private double amount;
    private LocalDate receivedDate;

    public TPaymentHistory(Long id, Long customerId, String comment, double amount, LocalDate receivedDate) {
        this.id = id;
        this.customerId = customerId;
        this.comment = comment;
        this.amount = amount;
        this.receivedDate = receivedDate;
    }

    public TPaymentHistory() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Override
    public String toString() {
        return "TPaymentHistory{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", comment='" + comment + '\'' +
                ", amount=" + amount +
                ", receivedDate=" + receivedDate +
                '}';
    }
}
