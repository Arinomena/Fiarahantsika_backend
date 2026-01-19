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

    /** Construit les OrderItem à partir de la requête */
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

            OrderItem line = new OrderItem();
            line.setOrder(order);
            line.setItemType(req.getItemType() != null ? req.getItemType() : ItemType.PRODUCT);
            line.setItemId(req.getItemId());
            line.setQuantity(req.getQuantity());
            line.setUnitPrice(req.getUnitPrice() != null ? req.getUnitPrice() : product.getPricePerUnit());

            // ✅ si lineTotal est fourni par le front, on le prend, sinon on recalcule
            if (req.getLineTotal() != null) {
                line.setLineTotal(req.getLineTotal());
            } else if (line.getUnitPrice() != null && line.getQuantity() != null) {
                line.setLineTotal(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
            } else {
                line.setLineTotal(BigDecimal.ZERO);
            }

            lines.add(line);
        }

        return lines;
    }

    /** Convertit une Order en OrderDTO */
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
                o.getDestination(),
                o.getVolumeCl(),
                o.getWeightKg(),
                o.getEmballageFee(),
                items
        );
    }

    /** Convertit une OrderItem en OrderItemDTO */
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
