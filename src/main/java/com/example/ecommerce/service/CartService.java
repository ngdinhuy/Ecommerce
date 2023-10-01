package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    public Cart addCart(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart findCartByUserId(Integer id){
        return cartRepository.findCartByUserId(id);
    }

    public Integer getCartIdByUserId(Integer idUser){
        Cart cart = cartRepository.findCartByUserId(idUser);
        if (cart == null){
            return null;
        } else {
            return cart.getId();
        }
    }
    public Boolean checkUserHasCart(Integer id){
        if (cartRepository.findCartsByUserId(id).isEmpty()){
            return true;
        }
        return false;
    }


}
