package com.fiarahantsika.backend.orders.repositories;

import com.fiarahantsika.backend.orders.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}
