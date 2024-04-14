package com.example.ecomerceapp1.models;

import java.io.Serializable;

public class ViewAllProductsModel implements Serializable {
    private String name;
    private String description;
    private String rating;
    private String img_url;
    private String type;
    private String documentId;

    private int price;

    public ViewAllProductsModel() {
    }

    public ViewAllProductsModel(String name, String description, String rating, String img_url, String type, int price, String documentId) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.img_url = img_url;
        this.type = type;
        this.price = price;
        this.documentId = documentId;

    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ViewAllProductsModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rating='" + rating + '\'' +
                ", img_url='" + img_url + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }
}
