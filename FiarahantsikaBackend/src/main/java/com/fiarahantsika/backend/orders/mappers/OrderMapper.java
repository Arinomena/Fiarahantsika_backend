package com.fiarahantsika.backend.orders.mappers;

import com.fiarahantsika.backend.common.enums.ItemType;
import com.fiarahantsika.backend.orders.dto.OrderDTO;
import com.fiarahantsika.backend.orders.dto.OrderItemDTO;
import com.fiarahantsika.backend.orders.dto.OrderItemRequest;
import com.fiarahantsika.backend.orders.entities.Order;
import com.fiarahantsika.backend.orders.entities.OrderItem;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class OrderMapper {

    private OrderMapper() {}

    /** Crée les OrderItem à partir de la requête */
    public static List<OrderItem> buildItems(
            Order order,
            List<OrderItemRequest> requests,
            ProductRepository productRepo
    ) {
        List<OrderItem> lines = new ArrayList<>();

        for (OrderItemRequest req : requests) {
            Product product = productRepo.findById(req.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Produit introuvable : " + req.getItemId()));

            // packaging (cageots)
            if (req.getCrateQty() > 0) {
                BigDecimal priceGroup = product.getPricePerGroup();
                BigDecimal lineTotal = priceGroup
                        .multiply(BigDecimal.valueOf(req.getCrateQty()));

                OrderItem line = new OrderItem();
                line.setOrder(order);
                line.setItemType(ItemType.PACKAGING);
                line.setItemId(product.getId());
                line.setQuantity(req.getCrateQty());
                line.setUnitPrice(priceGroup);
                line.setLineTotal(lineTotal);

                lines.add(line);
            }

            // produit à l’unité (bouteilles)
            if (req.getBottleQty() > 0) {
                BigDecimal priceUnit = product.getPricePerUnit();
                BigDecimal lineTotal = priceUnit
                        .multiply(BigDecimal.valueOf(req.getBottleQty()));

                OrderItem line = new OrderItem();
                line.setOrder(order);
                line.setItemType(ItemType.PRODUCT);
                line.setItemId(product.getId());
                line.setQuantity(req.getBottleQty());
                line.setUnitPrice(priceUnit);
                line.setLineTotal(lineTotal);

                lines.add(line);
            }
        }

        return lines;
    }

    /** Mappe Order → OrderDTO */
    public static OrderDTO toDto(Order o) {
        var items = o.getItems().stream()
                .map(OrderMapper::itemToDto)
                .collect(Collectors.toList());

        return new OrderDTO(
                o.getId(),
                o.getUser().getUsername(),
                o.getClient() != null ? o.getClient().getId() : null,
                o.getCreatedAt(),
                o.getTotal(),
                o.getStatus(),
                items
        );
    }

    private static OrderItemDTO itemToDto(OrderItem i) {
        return new OrderItemDTO(
                i.getItemType(),
                i.getItemId(),
                i.getQuantity(),
                i.getUnitPrice(),
                i.getLineTotal()
        );
    }
}
