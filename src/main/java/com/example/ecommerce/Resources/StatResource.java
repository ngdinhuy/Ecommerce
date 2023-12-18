package com.example.ecommerce.Resources;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.StatisticMonthly;
import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.*;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/statistic")
public class StatResource {

    @Autowired
    StatisticMonthlyService statisticMonthlyService;

    @Autowired
    UserService userService;

    @Autowired
    ProdcutService prodcutService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/monthly_income")
    ResponseEntity<BaseResponse> getMonthlyListIncome(@RequestParam("idSeller") Integer idSeller){
        User user = userService.findUserById(idSeller);
        if (user == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{Define.NOT_FOUND}, null);
        }
        List<StatisticMonthly> statisticMonthlyList = statisticMonthlyService.getListStatisticMonthly(user);
        HashMap<String, Double> response = new HashMap<>();
        for (StatisticMonthly statisticMonthly: statisticMonthlyList){
            response.put(statisticMonthly.getMonth(), statisticMonthly.getIncome());
        }
        return Utils.getResponse(Define.ERROR_CODE, new String[]{}, response);
    }

    @GetMapping("/statistic_current_month")
    ResponseEntity<BaseResponse> getOrderCurrentMonth(@RequestParam("idSeller") Integer idSeller, @RequestParam("type") Integer type){
        User user = userService.findUserById(idSeller);
        if (user == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{Define.NOT_FOUND}, null);
        }

        if (type == Define.StatisticType.ORDER_CURRENT_MONTH){
            //lấy ra số lượng đơn hàng trong tháng hiện tại
            List<Product> products = prodcutService.getProductBySellerId(user.getId());
            Integer numberOrder = 0;
            String currentMonth = Utils.getCurrentMonth();
//            for(Product product: products){
//                List<OrderItem> listOrderItem = orderItemService.getListOrderItemByProduct(product);
//                for (OrderItem orderItem: listOrderItem){
//                    if (orderItem.getOrder().getDate().contains(currentDate)){
//                        numberOrder += orderItem.getQuantity();
//                    }
//                }
//            }
            List<OrderItem> orderItems = orderItemService.getAllOrderItem();
            for (OrderItem orderItem: orderItems){
                if (orderItem.getProduct().getSellerid().getId() == idSeller){
                    if (orderItem.getOrder().getDate().contains(currentMonth)){
                        numberOrder++;
                    }
                }
            }
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, numberOrder);
        }
        //Lấy ra đánh giá trung bình của tất cả sản phẩm
        List<Product> products = prodcutService.getProductBySellerId(user.getId());
        Double totalRate = 0.0;
        for (Product product: products){
            totalRate += product.getRate();
        }
        if (products.isEmpty()){
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{"List product is empty"}, null);
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, Utils.formatDouble(totalRate/products.size()));
    }
}
