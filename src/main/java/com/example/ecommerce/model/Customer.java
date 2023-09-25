package com.example.ecommerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_customer")
public class Customer extends User{
    public Customer() {
    }
    

//    @Override
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public int getId() {
//        return super.getId();
//    }

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
}
