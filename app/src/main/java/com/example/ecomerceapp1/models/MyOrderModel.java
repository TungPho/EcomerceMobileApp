package com.example.ecomerceapp1.models;

public class MyOrderModel {
    String totalQuantity;
    int totalPrice;
    String productName;
    String productPrice;
    String address;
    String currentDate;
    String currentTime;
    String phoneNumber;
    String username;
    String img_url;

    String documentId;

    public MyOrderModel() {
    }

    @Override
    public String toString() {
        return "MyOrderModel{" +
                "totalQuantity='" + totalQuantity + '\'' +
                ", totalPrice=" + totalPrice +
                ", productName='" + productName + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", address='" + address + '\'' +
                ", currentDate='" + currentDate + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", username='" + username + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }

    public MyOrderModel(String totalQuantity, int totalPrice, String productName, String productPrice, String address, String currentDate, String currentTime, String phoneNumber, String username, String img_url, String documentId) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.productName = productName;
        this.productPrice = productPrice;
        this.address = address;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.img_url = img_url;
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
