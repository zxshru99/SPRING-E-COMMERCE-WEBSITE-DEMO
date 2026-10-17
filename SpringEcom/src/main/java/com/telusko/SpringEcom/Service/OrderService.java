package com.telusko.SpringEcom.Service;

import com.telusko.SpringEcom.Model.Order;
import com.telusko.SpringEcom.Model.OrderItem;
import com.telusko.SpringEcom.Model.dto.OrderRequest;
import com.telusko.SpringEcom.Model.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service

public class OrderService {
    public OrderResponse addProduct(OrderRequest request) {
        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());
        return null;


        List<OrderItem> orderItems=new ArrayList<>();
       for(Order)


    }

    public List<OrderResponse> getAllOrderResponses() {
    return null;
    }

}
