package com.example.ecommerce.Resources;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.response.OrderItemResponse;
import com.example.ecommerce.service.OrderItemService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order_item")
public class OrderItemResource {
    @Autowired
    OrderItemService orderItemService;

    @Autowired
    UserService userService;

    @GetMapping("/detail")
    ResponseEntity<BaseResponse> getDetailOrderItem(@RequestParam Integer id) {
        OrderItem orderItem = orderItemService.getDetailOrderItem(id);
        if (orderItem == null) {
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Order item is not exist"}, null);
        } else {
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, orderItem);
        }
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse> getListOrderItemByIdSeller(@RequestParam Integer idSeller) {
        User user = userService.findUserById(idSeller);
        if (user == null) {
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Seller is not exist"}, null);
        }
        List<OrderItem> orderItems = orderItemService.getAllOrderItem();
        ArrayList<OrderItem> response = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getSellerid().getId() == idSeller) {
                response.add(orderItem);
            }
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, response);
    }

    @GetMapping("/list_1")
    ResponseEntity<BaseResponse> getListOrderItemByIdSeller1(@RequestParam Integer idSeller) {
        User user = userService.findUserById(idSeller);
        if (user == null) {
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Seller is not exist"}, null);
        }
        List<OrderItemResponse> response = orderItemService.getOrderItemBySellerId(idSeller);
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, response);
    }
}
