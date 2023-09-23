package com.example.ecommerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_table")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


}
