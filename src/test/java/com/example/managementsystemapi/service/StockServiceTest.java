package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.domain.Stock;
import com.example.managementsystemapi.domain.StockMovement;
import com.example.managementsystemapi.dto.stock.StockEntryRequestDTO;
import com.example.managementsystemapi.dto.stock.StockExitRequestDTO;
import com.example.managementsystemapi.dto.stock.StockMovementResponseDTO;
import com.example.managementsystemapi.dto.stock.StockResponseDTO;
import com.example.managementsystemapi.exception.InsufficientStockException;
import com.example.managementsystemapi.mapper.StockMapper;
import com.example.managementsystemapi.mapper.StockMovementMapper;
import com.example.managementsystemapi.repository.StockMovementRepository;
import com.example.managementsystemapi.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository repository;

    @Mock
    private StockMovementRepository movementRepository;

    @Mock
    private StockMapper mapper;

    @Mock
    private StockMovementMapper movementMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private StockService service;

    @Test
    void shouldCreateStockIfNotExists() {

        Product product = new Product();
        product.setId(1L);

        Stock stock = Stock.createFor(product);

        when(productService.findOrThrow(1L))
                .thenReturn(product);

        when(repository.findByProductId(1L))
                .thenReturn(Optional.empty());

        when(repository.save(any()))
                .thenReturn(stock);

        when(mapper.toDTO(any()))
                .thenReturn(new StockResponseDTO());

        StockResponseDTO response =
                service.createStockIfNotExists(1L);

        assertNotNull(response);

        verify(repository).save(any());
    }

    @Test
    void shouldReturnExistingStockWithoutCreatingNewOne() {

        Product product = new Product();
        product.setId(1L);

        Stock stock = Stock.createFor(product);

        when(productService.findOrThrow(1L))
                .thenReturn(product);

        when(repository.findByProductId(1L))
                .thenReturn(Optional.of(stock));

        when(mapper.toDTO(any()))
                .thenReturn(new StockResponseDTO());

        service.createStockIfNotExists(1L);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldRegisterStockEntry() {

        Product product = new Product();
        product.setId(1L);

        Stock stock = Stock.createFor(product);

        StockEntryRequestDTO request =
                new StockEntryRequestDTO();

        request.setQuantity(10);
        request.setReason("Restock");

        when(productService.findOrThrow(1L))
                .thenReturn(product);

        when(repository.findByProductIdForUpdate(1L))
                .thenReturn(Optional.of(stock));

        when(movementRepository.save(any()))
                .thenReturn(new StockMovement());

        when(movementMapper.toDTO(any()))
                .thenReturn(new StockMovementResponseDTO());

        service.registerEntry(1L, request);

        assertEquals(10, stock.getQuantity());

        verify(repository).save(stock);
    }

    @Test
    void shouldRegisterManualExit() {

        Product product = new Product();
        product.setId(1L);

        Stock stock = Stock.createFor(product);
        stock.addEntry(20);

        var request = new StockExitRequestDTO();

        request.setQuantity(5);
        request.setReason("Damaged");

        when(productService.findOrThrow(1L))
                .thenReturn(product);

        when(repository.findByProductIdForUpdate(1L))
                .thenReturn(Optional.of(stock));

        when(movementRepository.save(any()))
                .thenReturn(new StockMovement());

        when(movementMapper.toDTO(any()))
                .thenReturn(new StockMovementResponseDTO());

        service.registerManualExit(1L, request);

        assertEquals(15, stock.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {

        Product product = new Product();
        product.setId(1L);

        Stock stock = Stock.createFor(product);

        var request = new StockExitRequestDTO();

        request.setQuantity(10);
        request.setReason("Damaged");

        when(productService.findOrThrow(1L))
                .thenReturn(product);

        when(repository.findByProductIdForUpdate(1L))
                .thenReturn(Optional.of(stock));

        assertThrows(
                InsufficientStockException.class,
                () -> service.registerManualExit(1L, request)
        );
    }
}