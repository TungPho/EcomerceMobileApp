package com.example.ecomerceapp1.models;

public class RecommendProducts {
    private String name;
    private String img_url;
    private String decription;
    private String price;
    private String rating;

    public RecommendProducts() {
    }

    public RecommendProducts(String name, String img_url, String decription, String price, String rating) {
        this.name = name;
        this.img_url = img_url;
        this.decription = decription;
        this.price = price;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}


