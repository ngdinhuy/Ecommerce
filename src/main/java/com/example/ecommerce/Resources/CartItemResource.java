package com.example.ecommerce.Resources;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.CartItemService;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/cart")
public class CartItemResource {
    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @GetMapping(path = "/all/{idUser}")
    ResponseEntity<BaseResponse> getAllCartItem(@PathVariable Integer idUser){
        Integer idCart = cartService.getCartIdByUserId(idUser);
        if (idCart == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{Define.USER_CART_IS_NOT_EXIST}, null);
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, cartItemService.getAllCartItem(idCart));
    }

    @PostMapping(path = "/update_quantity")
    ResponseEntity<BaseResponse> updateQuantity(@RequestBody MultiValueMap<String, String> request){
        Integer idCartItem = Integer.parseInt(Objects.requireNonNull(request.getFirst("id_cart_item")));
        Integer quantityChange = Integer.parseInt(Objects.requireNonNull(request.getFirst("quantity_change")));
        Integer idUser = Integer.parseInt(Objects.requireNonNull(request.getFirst("id_user")));

        CartItem cartItem = cartItemService.getCartItemById(idCartItem);
        if (cartItem == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"Cart item is not exist"}, null);
        }
        try{
            CartItem cartItemResponse = cartItemService.changeQuantityProduct(cartItem, quantityChange);
            return getAllCartItem(idUser);
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @DeleteMapping(path = "")
    ResponseEntity<BaseResponse> deleteCartItem(@RequestParam("id_cart_item") Integer idCartItem, @RequestParam("id_user") Integer idUser){
        CartItem cartItem = cartItemService.getCartItemById(idCartItem);
        if (cartItem == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"Cart item is not exist"}, false);
        }
        try {
            cartItemService.deleteCartItemById(idCartItem);
            return getAllCartItem(idUser);
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @GetMapping("/verify_cart")
    public ResponseEntity<BaseResponse> verifyCart(@RequestParam Integer idUser) {
        //verify user
        User user = userService.findUserById(idUser);
        if (user == null) {
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"id user is incorrect"}, null);
        }
        //verify cart
        Integer idCart = cartService.getCartIdByUserId(idUser);
        if (idCart == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{Define.USER_CART_IS_NOT_EXIST}, null);
        }
        List<CartItem> cartItems = cartItemService.getAllCartItem(idCart);
        if (cartItems.isEmpty()) {
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"cart is empty"}, null);
        }
        for(CartItem cartItem : cartItems){
            if (cartItem.getQuantity() > cartItem.getProduct().getQuantity()) {
                String error = "Product " + cartItem.getProduct().getName() + " doesn't have enough quantity.";
                return Utils.getResponse(Define.ERROR_CODE, new String[]{error}, null);
            }
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, true);
    }
}
