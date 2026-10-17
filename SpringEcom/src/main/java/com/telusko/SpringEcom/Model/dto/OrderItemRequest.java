package com.telusko.SpringEcom.Model.dto;


public record OrderItemRequest(
        int productId,
        int quantity
) {}