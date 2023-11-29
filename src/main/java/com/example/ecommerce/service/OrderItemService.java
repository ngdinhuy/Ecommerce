package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.response.OrderItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Log4j2
public class OrderItemService {
    @Autowired
    private OrderItemRepository repository;

    public OrderItem getDetailOrderItem(Integer idOrderItem){
        return repository.getOrderItemById(idOrderItem);
    }

    public OrderItem addOrderItem(OrderItem orderItem){
        return repository.save(orderItem);
    }

    public List<OrderItem> getListOrderItem(int idOrder){
        return repository.getOrderItemsByOrderId(idOrder);
    }

    public List<OrderItem> getListOrderItemByProduct(Product product){
        return repository.getOrderItemsByProduct(product);
    }

    public OrderItem updateOrderItem(OrderItem orderItem){
        return repository.save(orderItem);
    }

    public List<OrderItem> getAllOrderItem(){
        return repository.findAll();
    }

    public ArrayList<OrderItemResponse> getOrderItemBySellerId(Integer idSeller){
        ArrayList<OrderItemResponse> response = new ArrayList<>();
        for (Object[] result : repository.getOrderItemsByIdSeller(idSeller)){
            String images = (String) result[9];
            String[] listImage = new String[]{};
            if (images != null && !images.isBlank()){
                listImage = images.split(";");
            }

            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    Integer.parseInt(result[0].toString()),
                    Integer.parseInt(result[1].toString()),
                    Integer.parseInt(result[2].toString()),
                    result[3].toString(),
                    (Double) result[4],
                    (Integer) result[5],
                    (Integer) result[6],
                    result[8].toString(),
                    (Double) result[7],
                    Arrays.stream(listImage).toList()
            );
            response.add(orderItemResponse);
        }
        return response;
    }

}
