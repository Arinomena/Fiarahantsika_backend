package com.fiarahantsika.backend.orders.repositories;

import com.fiarahantsika.backend.orders.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository    extends JpaRepository<Order,Long> {

}
