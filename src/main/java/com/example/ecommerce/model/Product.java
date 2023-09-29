package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Entity
@Table(name = "tbl_product")
public class Product implements Serializable {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private int quantity;

    private Double price;

    private int discount;

    private String reviewNumber;

    private double rate;

    private String image;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

//    public Product(String name, String descriptiopn, int quantity, int price, int discount, String reviewNumber, double rate, String image, Seller sellerid) {
//        this.name = name;
//        this.descriptiopn = descriptiopn;
//        this.quantity = quantity;
//        this.price = price;
//        this.discount = discount;
//        this.reviewNumber = reviewNumber;
//        this.rate = rate;
//        this.image = image;
//        this.sellerid = sellerid;
//    }

    public Product() {
    }

    public void setId(int id) {
        this.id = id;
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

    public void setDescription(String descriptiopn) {
        this.description = descriptiopn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getReviewNumber() {
        return reviewNumber;
    }

    public void setReviewNumber(String reviewNumber) {
        this.reviewNumber = reviewNumber;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getSellerid() {
        return user;
    }

    public void setSellerid(User sellerid) {
        this.user = sellerid;
    }
}
