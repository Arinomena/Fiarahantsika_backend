package com.fiarahantsika.backend.orders.services;

import com.fiarahantsika.backend.catalog.dto.CreatePackagingExitRequest;
import com.fiarahantsika.backend.catalog.dto.CreateStockEntryRequest;
import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.repositories.PackagingRepository;
import com.fiarahantsika.backend.catalog.services.IStockEntryService;
import com.fiarahantsika.backend.common.enums.ItemType;
import com.fiarahantsika.backend.common.enums.OrderStatus;
import com.fiarahantsika.backend.common.enums.PackagingFormat;
import com.fiarahantsika.backend.orders.dto.CreateOrderRequest;
import com.fiarahantsika.backend.orders.dto.OrderDTO;
import com.fiarahantsika.backend.orders.dto.OrderItemRequest;
import com.fiarahantsika.backend.common.enums.GroupType;
import com.fiarahantsika.backend.orders.entities.Order;
import com.fiarahantsika.backend.orders.entities.OrderItem;
import com.fiarahantsika.backend.orders.mappers.OrderMapper;
import com.fiarahantsika.backend.orders.repositories.OrderItemRepository;
import com.fiarahantsika.backend.orders.repositories.OrderRepository;
import com.fiarahantsika.backend.clients.entities.Client;
import com.fiarahantsika.backend.clients.repositories.ClientRepository;
import com.fiarahantsika.backend.users.entities.User;
import com.fiarahantsika.backend.users.repositories.UserRepository;
import com.fiarahantsika.backend.catalog.repositories.ProductRepository;
import com.fiarahantsika.backend.catalog.services.IStockExitService;
import com.fiarahantsika.backend.catalog.services.IPackagingExitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.fiarahantsika.backend.orders.mappers.OrderMapper.toDto;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository       orderRepo;
    private final OrderItemRepository   itemRepo;
    private final UserRepository        userRepo;
    private final ClientRepository      clientRepo;
    private final ProductRepository     productRepo;
    private final IStockExitService     stockExitSvc;
    private final IStockEntryService stockEntrySvc;
    private final IPackagingExitService pkgExitSvc;
    private final PackagingRepository packagingRepo;

    @Override
    public OrderDTO createOrder(CreateOrderRequest req, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        Client client = null;
        if (!req.isDirectSale()) {
            client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));
        }

        Order order = new Order();
        order.setCreatedAt(Instant.now());
        order.setDirectSale(req.isDirectSale());
        order.setStatus(OrderStatus.ENREGISTREE);
        order.setUser(user);
        order.setClient(client);

        // Délégation au mapper
        List<OrderItem> lines = OrderMapper.buildItems(
                order,
                req.getItems(),
                productRepo
        );
        order.setItems(lines);

        BigDecimal total = lines.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Order saved = orderRepo.save(order);
        itemRepo.saveAll(lines);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        return orderRepo.findById(id)
                .map(OrderMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDTO updateOrder(Long id, CreateOrderRequest req, String username) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        order.setUser(user);
        order.setDirectSale(req.isDirectSale());
        if (req.isDirectSale()) {
            order.setClient(null);
        } else {
            Client client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));
            order.setClient(client);
        }

        // clear + rebuild items
        order.getItems().clear();
        List<OrderItem> lines = OrderMapper.buildItems(
                order,
                req.getItems(),
                productRepo
        );
        order.getItems().addAll(lines);

        BigDecimal total = lines.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Order saved = orderRepo.save(order);
        // pas besoin de itemRepo.saveAll thanks cascade + orphanRemoval
        return OrderMapper.toDto(saved);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    @Override
    public OrderDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable : " + id));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);

        // 1) À la livraison, on débite uniquement le stock produit (groupSize inclus)
        if (oldStatus == OrderStatus.ENREGISTREE
                && newStatus == OrderStatus.EN_COURS_DE_LIVRAISON) {

            order.getItems().forEach(i -> {
                Product prod = productRepo.findById(i.getItemId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Produit introuvable : " + i.getItemId()));

                // on passe la "quantité de groupes" à sortir
                int groupsToRemove = i.getItemType() == ItemType.PRODUCT
                        ? i.getQuantity()
                        : i.getQuantity() * prod.getGroupSize();

                stockExitSvc.recordExit(new CreateStockExitRequest(prod.getId(), groupsToRemove));
            });
        }

        // 2) À l'annulation après (ou pendant) livraison, on réintègre uniquement le stock produit
        if ((oldStatus == OrderStatus.EN_COURS_DE_LIVRAISON || oldStatus == OrderStatus.LIVREE)
                && newStatus == OrderStatus.ANNULEE) {

            order.getItems().forEach(i -> {
                Product prod = productRepo.findById(i.getItemId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Produit introuvable : " + i.getItemId()));

                // nombre de bouteilles à restaurer
                int bottlesToRestore = (i.getItemType() == ItemType.PRODUCT)
                        ? i.getQuantity()                         // 1 canette = 1 bouteille
                        : i.getQuantity() * prod.getGroupSize(); // 1 fardeau = groupSize bouteilles

                // on met à jour directement le stock
                prod.setCurrentStock(prod.getCurrentStock() + bottlesToRestore);
                productRepo.save(prod);

            });
        }

        // persistance et mapping en DTO
        Order saved = orderRepo.save(order);
        return toDto(saved);
    }
}
