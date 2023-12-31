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
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    StatisticMonthlyService statisticMonthlyService;

    @Autowired
    PaypalAuthService paypalAuthService;

    @PostMapping(path = "/insert")
    ResponseEntity<BaseResponse> addAllCartToOrder(@RequestBody MultiValueMap<String, String> request) {
        Integer idUser = Integer.parseInt(Objects.requireNonNull(request.getFirst("idUser")));
        Integer statePayment = Integer.parseInt(request.getFirst("state_payment")); //0 : chưa thanh toán, 1 : đã thanh toasn
        User user = userService.findUserById(idUser);
        if (user == null) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"User is not exist"}, null);
        }
        Cart cart = cartService.findCartByUserId(idUser);
        if (cart == null) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"Cart is not exist"}, null);
        }
        List<CartItem> cartItemList = cartItemService.getAllCartItem(cart.getId());
        Double total = 0.0;
        int quantity = 0;
        for (CartItem cartItem : cartItemList) {
            total += cartItem.getTotalPrice();
            quantity += cartItem.getQuantity();
        }
        try {
            Order order = orderService.addOrder(new Order(Utils.getCurrentDate(), total, quantity, 0.0, user));
            for (CartItem cartItem : cartItemList) {
                Product product = cartItem.getProduct();
                User seller = product.getSellerid();
                //thanh toán với paypal
//                Boolean responsePayment = false;
//                if (statePayment == Define.StatePayment.PAYMENT_WITH_PAYPAL && seller.getMailPaypal() != null && !seller.getMailPaypal().isBlank()) {
//                    String accessToken = paypalAuthService.getAccessToken();
////                    responsePayment = paypalAuthService.transferMoneyToSeller(cartItem.getTotalPrice().toString(), "sb-c4xfw26043972@personal.example.com", accessToken);
//                    responsePayment = paypalAuthService.transferMoneyToSeller(cartItem.getTotalPrice().toString(), seller.getMailPaypal(), accessToken);
//                } else {
//                    return Utils.getResponse(Define.ERROR_CODE, new String[]{"Seller don't have papal account!"}, null);
//                }
                // thêm tiền vào tài khoản user
                if (statePayment == 1){
                    seller.setProperty(seller.getProperty() + cartItem.getTotalPrice());
                    userService.updateUser(seller);
                }
                //thêm cho data thống kê
                statisticMonthlyService.addIncome(new StatisticMonthly(seller, Utils.getCurrentMonth(), product.getPriceProduct() * cartItem.getQuantity()));
                OrderItem orderItem = new OrderItem(cartItem.getQuantity(), product.getPriceProduct() * cartItem.getQuantity(), product, order);
                orderItem.setStatePayment(statePayment);
                orderItemService.addOrderItem(orderItem);
            }
            for (CartItem cartItem : cartItemList) {
                cartItemService.deleteCartItemById(cartItem.getId());
            }
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, true);
        } catch (Exception e) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @GetMapping("/all")
    ResponseEntity<BaseResponse> getAllOrder(@RequestParam(name = "id_user") int idUser) {
        List<Order> listOrder = orderService.getListOrderByUserId(idUser);
        if (listOrder.isEmpty()) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"List order is empty"}, null);
        } else {
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, listOrder);
        }
    }

    @GetMapping("/detail")
    ResponseEntity<BaseResponse> getListOrderItemByIdOrder(@RequestParam(name = "id_order") int idOrder) {
        List<OrderItem> listOrderItem = orderItemService.getListOrderItem(idOrder);
        if (listOrderItem.isEmpty()) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"List order item is empty"}, null);
        } else {
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, listOrderItem);
        }
    }
}
