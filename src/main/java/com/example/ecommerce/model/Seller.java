package com.example.ecommerce.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_seller")
public class Seller extends User {

//    private List<Product> products;
    public Seller() {
    }

//    @Override
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public int getId() {
//        return super.getId();
//    }

//
//    @Column
//    @Override
//    public String getName() {
//        return super.getName();
//    }
//
//    @Column
//    @Override
//    public String getEmail() {
//        return super.getEmail();
//    }
//
//    @Column
//    @Override
//    public int getRole() {
//        return super.getRole();
//    }
//
//    @Column
//    @Override
//    public String getPhoneNumber() {
//        return super.getPhoneNumber();
//    }
//
//    @Column
//    @Override
//    public String getUsername() {
//        return super.getUsername();
//    }
//
//    @Column
//    @Override
//    public String getPassword() {
//        return super.getPassword();
//    }

//    @OneToMany(mappedBy = "seller")
//    public List<Product> getProducts() {
//        return products;
//    }
}
