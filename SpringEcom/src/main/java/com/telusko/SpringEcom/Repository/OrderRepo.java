package com.telusko.SpringEcom.Repository;

import com.telusko.SpringEcom.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Integer> {
    Optional<Order> findByOrderId(String orderId);

}
