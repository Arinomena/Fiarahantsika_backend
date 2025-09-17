package com.fiarahantsika.backend.stats.repository;

import com.fiarahantsika.backend.orders.entities.Order;
import com.fiarahantsika.backend.stats.dto.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.fiarahantsika.backend.stats.dto.SalesByDayDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT new com.fiarahantsika.backend.stats.dto.SalesByDayDTO(
            CAST(FUNCTION('DATE', o.createdAt) AS date),
            SUM(o.total)
        )
        FROM Order o
        GROUP BY FUNCTION('DATE', o.createdAt)
        ORDER BY FUNCTION('DATE', o.createdAt)
    """)
    List<SalesByDayDTO> getSalesByDay();

    @Query("""
        SELECT new com.fiarahantsika.backend.stats.dto.TopProductDTO(
            p.id, p.name,
            SUM(CASE WHEN oi.itemType = com.fiarahantsika.backend.common.enums.ItemType.PRODUCT THEN oi.quantity ELSE 0 END),
            SUM(oi.lineTotal)
        )
        FROM OrderItem oi
        JOIN Product p ON p.id = oi.itemId
        GROUP BY p.id, p.name
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<TopProductDTO> getTopProducts();

    @Query("""
        SELECT new com.fiarahantsika.backend.stats.dto.InvoiceStatusDTO(
            i.status, COUNT(i), SUM(i.totalAmount)
        )
        FROM Invoice i
        GROUP BY i.status
    """)
    List<InvoiceStatusDTO> getInvoiceStatusStats();

    @Query("""
        SELECT new com.fiarahantsika.backend.stats.dto.AverageBasketDTO(
            AVG(o.total)
        )
        FROM Order o
    """)
    AverageBasketDTO getAverageBasket();

    @Query("""
        SELECT new com.fiarahantsika.backend.stats.dto.SalesByClientDTO(
            c.id, c.name, SUM(o.total)
        )
        FROM Order o
        JOIN o.client c
        GROUP BY c.id, c.name
        ORDER BY SUM(o.total) DESC
    """)
    List<SalesByClientDTO> getSalesByClient();

    @Query("""
        SELECT new com.fiarahantsika.backend.stats.dto.SalesBySellerDTO(
            u.id, u.username,
            COUNT(o.id),
            SUM(o.total)
        )
        FROM Order o
        JOIN o.user u
        WHERE MONTH(o.createdAt) = MONTH(CURRENT_DATE)
          AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)
        GROUP BY u.id, u.username
        ORDER BY SUM(o.total) DESC
    """)
    List<SalesBySellerDTO> getSalesBySellerCurrentMonth();

    @Query("""
        SELECT new com.fiarahantsika.backend.stats.dto.SalesBySellerDTO(
            u.id, u.username,
            COUNT(o.id),
            SUM(o.total)
        )
        FROM Order o
        JOIN o.user u
        WHERE o.createdAt BETWEEN :startDate AND :endDate
        GROUP BY u.id, u.username
        ORDER BY SUM(o.total) DESC
    """)
    List<SalesBySellerDTO> getSalesBySellerBetween(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
}
