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

    private String descriptiopn;

    private int quantity;

    private int price;

    private int discount;

    private String reviewNumber;

    private double rate;

    private String image;

    @ManyToOne()
    @JoinColumn(name = "seller_id")
    private Seller seller;

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

    public String getDescriptiopn() {
        return descriptiopn;
    }

    public void setDescriptiopn(String descriptiopn) {
        this.descriptiopn = descriptiopn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public Seller getSellerid() {
        return seller;
    }

    public void setSellerid(Seller sellerid) {
        this.seller = sellerid;
    }
}
