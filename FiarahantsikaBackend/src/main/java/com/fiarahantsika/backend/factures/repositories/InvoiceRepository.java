package com.fiarahantsika.backend.factures.repositories;

import com.fiarahantsika.backend.factures.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByOrderId(Long orderId);
}
