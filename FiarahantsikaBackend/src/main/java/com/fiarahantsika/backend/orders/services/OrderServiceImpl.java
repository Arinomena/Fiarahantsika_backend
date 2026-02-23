package com.fiarahantsika.backend.orders.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockEntryRequest;
import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.repositories.PackagingRepository;
import com.fiarahantsika.backend.catalog.services.IStockEntryService;
import com.fiarahantsika.backend.common.enums.DestinationType;
import com.fiarahantsika.backend.common.enums.ItemType;
import com.fiarahantsika.backend.common.enums.OrderStatus;
import com.fiarahantsika.backend.orders.dto.CreateOrderRequest;
import com.fiarahantsika.backend.orders.dto.OrderDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.fiarahantsika.backend.orders.mappers.OrderMapper.toDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final UserRepository userRepo;
    private final ClientRepository clientRepo;
    private final ProductRepository productRepo;
    private final IStockExitService stockExitSvc;
    private final IStockEntryService stockEntrySvc;
    private final IPackagingExitService pkgExitSvc;
    private final PackagingRepository packagingRepo;

    @Override
    public OrderDTO createOrder(CreateOrderRequest req, String username) {
        Logger log = LoggerFactory.getLogger(getClass());

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        Order order = new Order();
        order.setCreatedAt(Instant.now());
        order.setDirectSale(req.isDirectSale());
        order.setStatus(req.getStatus() != null ? req.getStatus() : OrderStatus.ENREGISTREE);
        order.setUser(user);
        order.setDestination(req.getDestination());

        if (!req.isDirectSale() && req.getClientId() != null) {
            order.setClient(clientRepo.findById(req.getClientId()).orElse(null));
        }

        List<OrderItem> lines = OrderMapper.buildItems(order, req.getItems(), productRepo);

        lines.forEach(i -> {
            if (i.getUnitPrice() == null || i.getQuantity() <= 0) {
                throw new IllegalArgumentException("Données d'article invalides");
            }
            i.setLineTotal(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        });

        order.setItems(lines);

        BigDecimal calculatedTotal = lines.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(req.getTotal() != null && req.getTotal().compareTo(BigDecimal.ZERO) > 0
                ? req.getTotal() : calculatedTotal);

        // --- GESTION DU STOCK (UNE SEULE FOIS ICI) ---
        for (OrderItem i : lines) {
            Product prod = productRepo.findById(i.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

            int inputQty = i.getQuantity();
            int groupSize = (prod.getGroupSize() != null && prod.getGroupSize() > 0) ? prod.getGroupSize() : 1;
            int totalUnits = (i.getItemType() == ItemType.GROUP) ? (inputQty * groupSize) : inputQty;

            if (req.getDestination() == DestinationType.FOURNISSEUR) {
                // ENTRÉE
                prod.setCurrentStock(prod.getCurrentStock() + totalUnits);
                productRepo.save(prod);
                stockEntrySvc.recordEntry(new CreateStockEntryRequest(prod.getId(), inputQty));
                log.info("STOCK FOURNISSEUR : +{} unités pour {}", totalUnits, prod.getName());
            }
            else if (req.getDestination() == DestinationType.CLIENT) {
                // SORTIE
                prod.setCurrentStock(prod.getCurrentStock() - totalUnits);
                productRepo.save(prod);
                // On enregistre totalUnits pour que stock_exit reflète la réalité des bouteilles
                stockExitSvc.recordExit(new CreateStockExitRequest(prod.getId(), totalUnits));
                log.info("STOCK CLIENT : -{} unités pour {}", totalUnits, prod.getName());
            }
        }

        Order saved = orderRepo.save(order);
        itemRepo.saveAll(lines);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        return orderRepo.findById(id).map(OrderMapper::toDto).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream().map(OrderMapper::toDto).toList();
    }

    @Override
    public OrderDTO updateOrder(Long id, CreateOrderRequest req, String username) {
        Order order = orderRepo.findById(id).orElseThrow();
        User user = userRepo.findByUsername(username).orElseThrow();
        order.setUser(user);
        order.setDirectSale(req.isDirectSale());
        if (req.isDirectSale()) { order.setClient(null); }
        else { order.setClient(clientRepo.findById(req.getClientId()).orElse(null)); }

        order.getItems().clear();
        List<OrderItem> lines = OrderMapper.buildItems(order, req.getItems(), productRepo);
        order.getItems().addAll(lines);
        order.setTotal(lines.stream().map(OrderItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add));

        return toDto(orderRepo.save(order));
    }

    @Override
    public void deleteOrder(Long id) { orderRepo.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersPage(Pageable pageable) {
        return orderRepo.findAll(pageable).map(OrderMapper::toDto);
    }

    @Override
    public OrderDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);

        // --- ON NE DÉDUIT PLUS RIEN ICI (DÉJÀ FAIT À LA CRÉATION) ---
        // ON GÈRE UNIQUEMENT LA RESTAURATION EN CAS D'ANNULATION
        if (oldStatus != OrderStatus.ANNULEE && newStatus == OrderStatus.ANNULEE) {
            order.getItems().forEach(i -> {
                Product prod = productRepo.findById(i.getItemId()).orElseThrow();
                int groupSize = (prod.getGroupSize() != null && prod.getGroupSize() > 0) ? prod.getGroupSize() : 1;
                int totalToRestore = (i.getItemType() == ItemType.PRODUCT) ? i.getQuantity() : i.getQuantity() * groupSize;

                prod.setCurrentStock(prod.getCurrentStock() + totalToRestore);
                productRepo.save(prod);
            });
        }

        Order saved = orderRepo.save(order);
        return toDto(saved);
    }
}