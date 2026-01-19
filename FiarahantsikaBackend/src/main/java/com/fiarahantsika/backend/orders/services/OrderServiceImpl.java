package com.fiarahantsika.backend.orders.services;

import com.fiarahantsika.backend.catalog.dto.CreateStockExitRequest;
import com.fiarahantsika.backend.catalog.entities.Product;
import com.fiarahantsika.backend.catalog.repositories.PackagingRepository;
import com.fiarahantsika.backend.catalog.services.IStockEntryService;
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
        order.setStatus(req.getStatus() != null ? req.getStatus() : OrderStatus.ENREGISTREE);
        order.setUser(user);
        order.setDestination(req.getDestination());
        order.setClient(client);
        order.setVolumeCl(req.getVolumeCl());
        order.setWeightKg(req.getWeightKg());
        order.setEmballageFee(req.getEmballageFee());

        List<OrderItem> lines = OrderMapper.buildItems(order, req.getItems(), productRepo);

        lines.forEach(i -> {
            if (i.getUnitPrice() != null && i.getQuantity() != null) {
                i.setLineTotal(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
            }
        });

        order.setItems(lines);

        BigDecimal total = lines.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (req.getTotal() != null && req.getTotal().compareTo(BigDecimal.ZERO) > 0) {
            order.setTotal(req.getTotal());
        } else {
            order.setTotal(total);
        }

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
        return OrderMapper.toDto(saved);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersPage(Pageable pageable) {
        return orderRepo.findAll(pageable).map(OrderMapper::toDto);
    }


    @Override
    public OrderDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable : " + id));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);

        if (oldStatus == OrderStatus.ENREGISTREE
                && newStatus == OrderStatus.EN_COURS_DE_LIVRAISON) {

            order.getItems().forEach(i -> {
                Product prod = productRepo.findById(i.getItemId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Produit introuvable : " + i.getItemId()));

                int groupsToRemove = i.getItemType() == ItemType.PRODUCT
                        ? i.getQuantity()
                        : i.getQuantity() * prod.getGroupSize();

                stockExitSvc.recordExit(new CreateStockExitRequest(prod.getId(), groupsToRemove));
            });
        }

        if ((oldStatus == OrderStatus.EN_COURS_DE_LIVRAISON || oldStatus == OrderStatus.LIVREE)
                && newStatus == OrderStatus.ANNULEE) {

            order.getItems().forEach(i -> {
                Product prod = productRepo.findById(i.getItemId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Produit introuvable : " + i.getItemId()));

                int bottlesToRestore = (i.getItemType() == ItemType.PRODUCT)
                        ? i.getQuantity()                         // 1 canette = 1 bouteille
                        : i.getQuantity() * prod.getGroupSize(); // 1 fardeau = groupSize bouteilles

                prod.setCurrentStock(prod.getCurrentStock() + bottlesToRestore);
                productRepo.save(prod);

            });
        }

         Order saved = orderRepo.save(order);
        return toDto(saved);
    }
}
