package com.example.ecommerce.Resources;

import com.example.ecommerce.model.*;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.*;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/order")
public class OrderResource {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    UserService userService;

    @Autowired
    ProdcutService productService;

    @Autowired
    CartService cartService;

    @PostMapping(path = "/insert")
    ResponseEntity<BaseResponse> addAllCartToOrder(@RequestBody MultiValueMap<String, String> request){
        Integer idUser = Integer.parseInt(Objects.requireNonNull(request.getFirst("idUser")));
        User user = userService.findUserById(idUser);
        if (user == null){
             return Utils.getResponse(Define.ERROR_CODE, new String[]{"User is not exist"}, null);
        }
        Cart cart = cartService.findCartByUserId(idUser);
        if (cart == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"Cart is not exist"}, null);
        }
        List<CartItem> cartItemList = cartItemService.getAllCartItem(cart.getId());
        int total = 0;
        for (CartItem cartItem: cartItemList){
            total += cartItem.getTotalPrice();
        }
        try{
            Order order = orderService.addOrder(new Order(Utils.getCurrentDate(), total, 0, user));
            for (CartItem cartItem: cartItemList){
                Product product = cartItem.getProduct();
                OrderItem orderItem = new OrderItem(cartItem.getQuantity(), product, order);
                orderItemService.addOrderItem(orderItem);
            }
            for (CartItem cartItem: cartItemList){
                cartItemService.deleteCartItemById(cartItem.getId());
            }
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, true);
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()},null);
        }

    }
}
