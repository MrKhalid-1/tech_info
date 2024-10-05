package com.example.tech.tech_info.entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class TPaymentHistory {
    private Long id;
    private Long customerId;
    private String comment;
    private Double amount;
    private LocalDate receivedDate;
    private String imageName;
    private String imagePath;

    public TPaymentHistory(Long id, Long customerId, String comment, Double amount, LocalDate receivedDate, String imageName, String imagePath) {
        this.id = id;
        this.customerId = customerId;
        this.comment = comment;
        this.amount = amount;
        this.receivedDate = receivedDate;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getComment() {
        return comment;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

//    public File downloadImage() throws IOException {
//        Path sourcePath = Path.of(imagePath);
//        File destinationFile = new File("downloads/" + imageName);
//        Files.copy(sourcePath, destinationFile.toPath());
//        return destinationFile;
//    }

    @Override
    public String toString() {
        return "TPaymentHistory{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", comment='" + comment + '\'' +
                ", amount=" + amount +
                ", receivedDate=" + receivedDate +
                ", imageName='" + imageName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
