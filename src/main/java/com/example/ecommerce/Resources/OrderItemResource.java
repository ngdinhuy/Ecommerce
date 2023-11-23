package com.example.ecommerce.Resources;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.OrderItemService;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order_item")
public class OrderItemResource {
    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/detail")
    ResponseEntity<BaseResponse> getDetailOrderItem(@RequestParam Integer id) {
        OrderItem orderItem = orderItemService.getDetailOrderItem(id);
        if (orderItem == null) {
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Order item is not exist"}, null);
        } else {
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, orderItem);
        }
    }
}
