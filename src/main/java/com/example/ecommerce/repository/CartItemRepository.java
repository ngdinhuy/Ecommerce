package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findCartItemByProductIdAndCartId(Integer idProduct, Integer idCart);

    List<CartItem> findCartItemsByCartId(Integer idCart);

    CartItem findCartItemById(Integer id);

    CartItem deleteCartItemById(Integer id);
}
