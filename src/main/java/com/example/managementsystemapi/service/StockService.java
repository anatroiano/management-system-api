package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.domain.Stock;
import com.example.managementsystemapi.domain.StockMovement;
import com.example.managementsystemapi.dto.StockEntryRequestDTO;
import com.example.managementsystemapi.dto.StockExitRequestDTO;
import com.example.managementsystemapi.dto.StockMovementResponseDTO;
import com.example.managementsystemapi.dto.StockResponseDTO;
import com.example.managementsystemapi.enums.MovementType;
import com.example.managementsystemapi.exception.NotFoundException;
import com.example.managementsystemapi.mapper.StockMapper;
import com.example.managementsystemapi.mapper.StockMovementMapper;
import com.example.managementsystemapi.repository.StockMovementRepository;
import com.example.managementsystemapi.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMovementRepository movementRepository;
    private final StockMapper stockMapper;
    private final StockMovementMapper movementMapper;
    private final ProductService productService;

    @Transactional
    public StockResponseDTO createStockIfNotExists(Long productId) {

        Product product = productService.findOrThrow(productId);

        return stockRepository.findByProductId(productId)
                .map(stockMapper::toDTO)
                .orElseGet(() -> {
                    Stock stock = Stock.createFor(product);
                    return stockMapper.toDTO(stockRepository.save(stock));
                });
    }

    @Transactional(readOnly = true)
    public StockResponseDTO getByProduct(Long productId) {
        return stockMapper.toDTO(findStockOrThrow(productId));
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponseDTO> getHistory(
            Long productId,
            MovementType type,
            Pageable pageable
    ) {

        Page<StockMovement> page = (type == null)
                ? movementRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable)
                : movementRepository.findByProductIdAndTypeOrderByCreatedAtDesc(productId, type, pageable);

        return page.map(movementMapper::toDTO);
    }

    @Transactional
    public StockMovementResponseDTO registerEntry(Long productId, StockEntryRequestDTO request) {
        return applyMovement(
                productId,
                MovementType.ENTRY,
                request.getQuantity(),
                request.getReason()
        );
    }

    @Transactional
    public void registerEntry(Long productId, Integer quantity, String reason) {
        applyMovement(
                productId,
                MovementType.ENTRY,
                quantity,
                reason
        );
    }

    @Transactional
    public StockMovementResponseDTO registerManualExit(Long productId, StockExitRequestDTO request) {
        return applyMovement(
                productId,
                MovementType.MANUAL_EXIT,
                request.getQuantity(),
                request.getReason()
        );
    }

    @Transactional
    public void registerSaleExit(Long productId, Long saleId, Integer quantity) {
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

        Product product = productService.findOrThrow(productId);
        Stock stock = findStockForUpdateOrThrow(productId);

        applyStockChange(stock, type, quantity);

        StockMovement movement = StockMovement.of(
                product,
                type,
                quantity,
                reason
        );

        stockRepository.save(stock);
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
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Stock not found for product id: " + productId
                ));
    }

    private Stock findStockForUpdateOrThrow(Long productId) {
        return stockRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundException(
                        "Stock not found for product id: " + productId
                ));
    }
}