package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Customer;
import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.domain.Sale;
import com.example.managementsystemapi.domain.SaleItem;
import com.example.managementsystemapi.dto.CreateSaleItemRequestDTO;
import com.example.managementsystemapi.dto.CreateSaleRequestDTO;
import com.example.managementsystemapi.dto.SaleResponseDTO;
import com.example.managementsystemapi.enums.SaleStatus;
import com.example.managementsystemapi.exception.NotFoundException;
import com.example.managementsystemapi.mapper.SaleMapper;
import com.example.managementsystemapi.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleService {

    private static final Logger log = LoggerFactory.getLogger(SaleService.class);

    private final SaleRepository saleRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final StockService stockService;
    private final SaleMapper saleMapper;

    public SaleResponseDTO create(CreateSaleRequestDTO request) {

        log.info("Creating sale - customerId: {}", request.getCustomerId());

        Customer customer = customerService.findOrThrow(request.getCustomerId());

        List<SaleItem> items = request.getItems()
                .stream()
                .map(this::buildSaleItem)
                .toList();

        BigDecimal totalAmount = calculateTotalAmount(items);

        Sale sale = buildSale(customer, items, totalAmount);

        items.forEach(item -> item.setSale(sale));

        Sale savedSale = saleRepository.save(sale);

        processStockOutput(savedSale, items);

        log.info("Sale created successfully - id: {}", savedSale.getId());

        return saleMapper.toDTO(savedSale);
    }

    @Transactional(readOnly = true)
    public SaleResponseDTO findOne(Long id) {

        log.info("Fetching sale by id: {}", id);

        return saleMapper.toDTO(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<SaleResponseDTO> findAll(Pageable pageable) {

        log.info("Fetching sales - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        return saleRepository.findAll(pageable)
                .map(saleMapper::toDTO);
    }

    public SaleResponseDTO cancel(Long id) {

        log.info("Canceling sale - id: {}", id);

        Sale sale = findOrThrow(id);

        if (SaleStatus.CANCELED.equals(sale.getStatus())) {

            log.warn("Sale already canceled - id: {}", id);

            throw new IllegalArgumentException("Sale is already canceled");
        }

        restoreStock(sale);

        sale.setStatus(SaleStatus.CANCELED);

        Sale canceledSale = saleRepository.save(sale);

        log.info("Sale canceled successfully - id: {}", id);

        return saleMapper.toDTO(canceledSale);
    }

    public Sale findOrThrow(Long id) {

        return saleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Sale not found - id: {}", id);

                    return new NotFoundException("Sale not found with id: " + id);
                });
    }

    private Sale buildSale(Customer customer, List<SaleItem> items, BigDecimal totalAmount) {

        Sale sale = new Sale();

        sale.setCustomer(customer);
        sale.setStatus(SaleStatus.COMPLETED);
        sale.setItems(items);
        sale.setTotalAmount(totalAmount);

        return sale;
    }

    private SaleItem buildSaleItem(CreateSaleItemRequestDTO request) {

        Product product = productService.findOrThrow(
                request.getProductId()
        );

        BigDecimal unitPrice = product.getPrice();

        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        SaleItem item = new SaleItem();

        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(unitPrice);
        item.setSubtotal(subtotal);

        log.info("Building sale item - productId: {}, quantity: {}, subtotal: {}", product.getId(), request.getQuantity(), subtotal);

        return item;
    }

    private BigDecimal calculateTotalAmount(List<SaleItem> items) {

        BigDecimal totalAmount = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Calculated total sale amount: {}", totalAmount);

        return totalAmount;
    }

    private void processStockOutput(Sale sale, List<SaleItem> items) {

        log.info("Processing stock output for sale - id: {}", sale.getId());

        items.forEach(item ->
                stockService.registerSaleExit(
                        item.getProduct().getId(),
                        sale.getId(),
                        item.getQuantity()
                )
        );
    }

    private void restoreStock(Sale sale) {

        log.info("Restoring stock for canceled sale - id: {}", sale.getId());

        sale.getItems().forEach(item ->
                stockService.registerEntry(
                        item.getProduct().getId(),
                        item.getQuantity(),
                        "Sale canceled"
                )
        );
    }

}