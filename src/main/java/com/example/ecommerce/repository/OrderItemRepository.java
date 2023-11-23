package com.example.ecommerce.repository;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> getOrderItemsByOrderId(Integer idOrder);

    List<OrderItem> getOrderItemsByProduct(Product product);

    OrderItem getOrderItemById(Integer idOrderItem);
}
