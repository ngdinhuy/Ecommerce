package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository repository;

    public OrderItem addOrderItem(OrderItem orderItem){
        return repository.save(orderItem);
    }

    public List<OrderItem> getListOrderItem(int idOrder){
        return repository.getOrderItemsByOrderId(idOrder);
    }

    public List<OrderItem> getListOrderItemByProduct(Product product){
        return repository.getOrderItemsByProduct(product);
    }

}
