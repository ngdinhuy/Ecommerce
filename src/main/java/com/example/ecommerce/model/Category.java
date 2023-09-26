package com.example.ecommerce.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "tbl_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)

    private String title;

    public Category() {
    }

    public Category(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    @ManyToMany
//    @JoinTable(name = "category_product",
//    joinColumns = @JoinColumn(name = "category_id"),
//    inverseJoinColumns = @JoinColumn(name = "product_id"))
//    private List<Product> products;

}
