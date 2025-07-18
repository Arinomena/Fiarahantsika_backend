package com.fiarahantsika.backend.orders.services;

import com.fiarahantsika.backend.common.enums.OrderStatus;
import com.fiarahantsika.backend.orders.dto.CreateOrderRequest;
import com.fiarahantsika.backend.orders.dto.OrderDTO;

import java.util.List;

public interface IOrderService {

    OrderDTO createOrder(CreateOrderRequest req, String username);

    OrderDTO getOrderById(Long id);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrder(Long id, CreateOrderRequest req, String username);

    void deleteOrder(Long id);

    OrderDTO updateStatus(Long id, OrderStatus newStatus);
}
