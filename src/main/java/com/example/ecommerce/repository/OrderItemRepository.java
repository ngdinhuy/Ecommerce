package com.example.ecommerce.repository;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> getOrderItemsByOrderId(Integer idOrder);

    List<OrderItem> getOrderItemsByProduct(Product product);

    OrderItem getOrderItemById(Integer idOrderItem);

    @Query(value = "SELECT o.id, o.state_payment, p.id, p.name, p.price, u.id, o.quantity, o.price, u.name, p.image " +
            "FROM tbl_order_item o " +
            "INNER JOIN tbl_product p ON o.product_id = p.id " +
            "INNER JOIN tbl_user u ON p.user_id = u.id " +
            "WHERE u.id = :id_seller", nativeQuery = true)
    List<Object[]> getOrderItemsByIdSeller(@Param("id_seller") Integer idSeller);

}
