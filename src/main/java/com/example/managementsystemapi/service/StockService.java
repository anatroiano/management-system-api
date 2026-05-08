package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.domain.Stock;
import com.example.managementsystemapi.domain.StockMovement;
import com.example.managementsystemapi.dto.stock.StockEntryRequestDTO;
import com.example.managementsystemapi.dto.stock.StockExitRequestDTO;
import com.example.managementsystemapi.dto.stock.StockMovementResponseDTO;
import com.example.managementsystemapi.dto.stock.StockResponseDTO;
import com.example.managementsystemapi.enums.MovementType;
import com.example.managementsystemapi.exception.NotFoundException;
import com.example.managementsystemapi.mapper.StockMapper;
import com.example.managementsystemapi.mapper.StockMovementMapper;
import com.example.managementsystemapi.repository.StockMovementRepository;
import com.example.managementsystemapi.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private static final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository repository;
    private final StockMovementRepository movementRepository;
    private final StockMapper mapper;
    private final StockMovementMapper movementMapper;
    private final ProductService productService;

    @Transactional
    public StockResponseDTO createStockIfNotExists(Long productId) {

        log.info("Creating stock if not exists - productId: {}", productId);

        Product product = productService.findOrThrow(productId);

        return repository.findByProductId(productId)
                .map(mapper::toDTO)
                .orElseGet(() -> {
                    log.info("Stock not found, creating new stock - productId: {}", productId);

                    Stock stock = Stock.createFor(product);

                    return mapper.toDTO(repository.save(stock));
                });
    }

    @Transactional(readOnly = true)
    public StockResponseDTO getByProduct(Long productId) {

        log.info("Fetching stock by product - productId: {}", productId);

        return mapper.toDTO(findStockOrThrow(productId));
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponseDTO> getHistory(Long productId, MovementType type, Pageable pageable) {

        log.info("Fetching stock movement history - productId: {}, type: {}, page: {}, size: {}", productId, type, pageable.getPageNumber(), pageable.getPageSize());

        Page<StockMovement> page = (type == null)
                ? movementRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable)
                : movementRepository.findByProductIdAndTypeOrderByCreatedAtDesc(
                productId,
                type,
                pageable
        );

        return page.map(movementMapper::toDTO);
    }

    @Transactional
    public StockMovementResponseDTO registerEntry(Long productId, StockEntryRequestDTO request) {

        log.info("Registering stock entry - productId: {}, quantity: {}", productId, request.getQuantity());

        return applyMovement(
                productId,
                MovementType.ENTRY,
                request.getQuantity(),
                request.getReason()
        );
    }

    @Transactional
    public void registerEntry(Long productId, Integer quantity, String reason) {

        log.info("Registering stock entry - productId: {}, quantity: {}", productId, quantity);

        applyMovement(
                productId,
                MovementType.ENTRY,
                quantity,
                reason
        );
    }

    @Transactional
    public StockMovementResponseDTO registerManualExit(Long productId, StockExitRequestDTO request) {

        log.info("Registering manual stock exit - productId: {}, quantity: {}", productId, request.getQuantity());

        return applyMovement(
                productId,
                MovementType.MANUAL_EXIT,
                request.getQuantity(),
                request.getReason()
        );
    }

    @Transactional
    public void registerSaleExit(Long productId, Long saleId, Integer quantity) {

        log.info("Registering sale stock exit - productId: {}, saleId: {}, quantity: {}", productId, saleId, quantity);

        applyMovement(
                productId,
                MovementType.SALE_EXIT,
                quantity,
                "Automatic exit on sale " + saleId
        );
    }

    private StockMovementResponseDTO applyMovement(
            Long productId,
            MovementType type,
            int quantity,
            String reason
    ) {

        log.info("Applying stock movement - productId: {}, type: {}, quantity: {}", productId, type, quantity);

        Product product = productService.findOrThrow(productId);

        Stock stock = findStockForUpdateOrThrow(productId);

        applyStockChange(stock, type, quantity);

        StockMovement movement = StockMovement.of(product, type, quantity, reason);

        repository.save(stock);

        log.info("Stock updated successfully - productId: {}, currentQuantity: {}", productId, stock.getQuantity());

        return movementMapper.toDTO(movementRepository.save(movement));
    }

    private void applyStockChange(Stock stock, MovementType type, int quantity) {
        switch (type) {
            case ENTRY -> stock.addEntry(quantity);

            case MANUAL_EXIT, SALE_EXIT -> stock.addExit(quantity);

            default -> throw new IllegalArgumentException("Unsupported movement type: " + type);
        }
    }

    private Stock findStockOrThrow(Long productId) {

        return repository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Stock not found for product id: " + productId
                ));
    }

    private Stock findStockForUpdateOrThrow(Long productId) {

        return repository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Stock not found for product id: " + productId
                ));
    }
}