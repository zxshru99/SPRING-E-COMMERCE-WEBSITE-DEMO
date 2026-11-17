package com.telusko.SpringEcom.Controller;

import com.telusko.SpringEcom.Model.dto.OrderRequest;
import com.telusko.SpringEcom.Model.dto.OrderResponse;
import com.telusko.SpringEcom.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.placeOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrderResponses() {
        List<OrderResponse> orderResponseList = orderService.getAllOrderResponses();
        return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }
}
