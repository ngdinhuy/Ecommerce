package com.example.ecommerce.service;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository repository;

    public CartItem addCartItem(CartItem cartItem){
        return repository.save(cartItem);
    }

    public List<CartItem> getAllCartItem(Integer idCart){
        return repository.findCartItemsByCartId(idCart);
    }

    public Boolean checkCartItemExist(Integer idProduct, Integer idCart){
        return repository.findCartItemByProductIdAndCartId(idProduct, idCart) != null;
    }

    public CartItem findCartItemByProductId(Integer idProduct, Integer idCart){
        return repository.findCartItemByProductIdAndCartId(idProduct, idCart);
    }

    public CartItem changeQuantityProduct(CartItem cartItem, Integer quantityChange){
        if (cartItem.getQuantity()+ quantityChange <=0){
//            return repository.deleteCartItemById(cartItem.getId());
            repository.deleteById(cartItem.getId());
            return null;
        }
        cartItem.setQuantity(cartItem.getQuantity() + quantityChange);
        cartItem.setTotalPrice(cartItem.getTotalPrice() + quantityChange*cartItem.getProduct().getPriceProduct());
        return repository.save(cartItem);
    }

    public CartItem getCartItemById(Integer id){
        return repository.findCartItemById(id);
    }

    public void deleteCartItemById(Integer id){
        repository.deleteById(id);
    }

}
