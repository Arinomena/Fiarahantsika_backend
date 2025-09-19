package com.fiarahantsika.backend.factures.services;

import com.fiarahantsika.backend.factures.dto.InvoiceDTO;
import com.fiarahantsika.backend.factures.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface IInvoiceService {
    InvoiceDTO create(InvoiceDTO dto);
    InvoiceDTO getById(Long id);
    List<InvoiceDTO> listByOrder(Long orderId);
    PaymentDTO addPayment(Long invoiceId, BigDecimal amount);
    List<InvoiceDTO> listAll();
    List<PaymentDTO> getPayments(Long invoiceId);
    Page<InvoiceDTO> getInvoicesPage(Pageable pageable);
}
