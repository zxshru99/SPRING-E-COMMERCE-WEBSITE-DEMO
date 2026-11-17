package com.telusko.SpringEcom.Service;

import com.telusko.SpringEcom.Model.Order;
import com.telusko.SpringEcom.Model.OrderItem;
import com.telusko.SpringEcom.Model.Product;
import com.telusko.SpringEcom.Model.dto.OrderItemRequest;
import com.telusko.SpringEcom.Model.dto.OrderRequest;
import com.telusko.SpringEcom.Model.dto.OrderResponse;
import com.telusko.SpringEcom.Repository.OrderRepo;
import com.telusko.SpringEcom.Repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderService orderService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1);
        sampleProduct.setName("Wireless Mouse");
        sampleProduct.setPrice(BigDecimal.valueOf(25.00));
        sampleProduct.setStockQuantity(10);
    }

    @Test
    void testPlaceOrderSuccess() {
        OrderItemRequest itemReq = new OrderItemRequest(1, 2);
        OrderRequest orderReq = new OrderRequest("Alice", "alice@test.com", List.of(itemReq));

        when(productRepo.findById(1)).thenReturn(Optional.of(sampleProduct));
        when(productRepo.save(any(Product.class))).thenReturn(sampleProduct);
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.placeOrder(orderReq);

        assertNotNull(response);
        assertEquals("Alice", response.customerName());
        assertEquals("alice@test.com", response.email());
        assertEquals("PLACED", response.status());
        assertEquals(8, sampleProduct.getStockQuantity());
        assertEquals(1, response.items().size());
        assertEquals("Wireless Mouse", response.items().get(0).productName());
        assertEquals(2, response.items().get(0).quantity());
        assertEquals(BigDecimal.valueOf(50.00), response.items().get(0).totalPrice());

        verify(productRepo).save(sampleProduct);
        verify(orderRepo).save(any(Order.class));
    }

    @Test
    void testPlaceOrderProductNotFound() {
        OrderItemRequest itemReq = new OrderItemRequest(99, 1);
        OrderRequest orderReq = new OrderRequest("Bob", "bob@test.com", List.of(itemReq));

        when(productRepo.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.placeOrder(orderReq));
        assertTrue(exception.getMessage().contains("Product not found"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void testPlaceOrderInsufficientStock() {
        OrderItemRequest itemReq = new OrderItemRequest(1, 20); // only 10 in stock
        OrderRequest orderReq = new OrderRequest("Charlie", "charlie@test.com", List.of(itemReq));

        when(productRepo.findById(1)).thenReturn(Optional.of(sampleProduct));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.placeOrder(orderReq));
        assertTrue(exception.getMessage().contains("Insufficient stock"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void testGetAllOrderResponses() {
        Order order = new Order();
        order.setOrderId("ORD12345");
        order.setCustomerName("David");
        order.setEmail("david@test.com");
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        OrderItem item = OrderItem.builder()
                .product(sampleProduct)
                .quantity(1)
                .totalPrice(BigDecimal.valueOf(25.00))
                .order(order)
                .build();
        order.setOrderItems(List.of(item));

        when(orderRepo.findAll()).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getAllOrderResponses();

        assertEquals(1, responses.size());
        assertEquals("ORD12345", responses.get(0).orderId());
        assertEquals("David", responses.get(0).customerName());
        assertEquals(1, responses.get(0).items().size());
    }
}
