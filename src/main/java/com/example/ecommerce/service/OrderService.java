package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    public Order addOrder(Order order){
        return repository.save(order);
    }

    public List<Order> getListOrderByUserId(Integer idUser){
        return repository.getOrdersByUserId(idUser);
    }
}
