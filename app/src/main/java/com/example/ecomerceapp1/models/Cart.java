package com.example.ecomerceapp1.models;

import java.io.Serializable;

public class Cart implements Serializable {
    String productName;
    String productPrice;
    String currentTime;
    String currentDate;
    String totalQuantity;
    int totalPrice;

    String documentId;


    public Cart() {
    }

    public Cart(String productName, String productPrice, String currentTime, String currentDate, String totalQuantity, int totalPrice, String documentId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
