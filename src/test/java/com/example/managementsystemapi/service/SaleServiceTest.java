package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.Customer;
import com.example.managementsystemapi.domain.Product;
import com.example.managementsystemapi.domain.Sale;
import com.example.managementsystemapi.domain.SaleItem;
import com.example.managementsystemapi.dto.sale.CreateSaleItemRequestDTO;
import com.example.managementsystemapi.dto.sale.CreateSaleRequestDTO;
import com.example.managementsystemapi.dto.sale.SaleResponseDTO;
import com.example.managementsystemapi.enums.SaleStatus;
import com.example.managementsystemapi.mapper.SaleMapper;
import com.example.managementsystemapi.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository repository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private StockService stockService;

    @Mock
    private SaleMapper mapper;

    @InjectMocks
    private SaleService service;

    @Test
    void shouldCreateSaleSuccessfully() {

        Customer customer = new Customer();
        customer.setId(1L);

        Product product = new Product();
        product.setId(10L);
        product.setPrice(BigDecimal.valueOf(100));

        CreateSaleItemRequestDTO itemRequest = new CreateSaleItemRequestDTO();
        itemRequest.setProductId(10L);
        itemRequest.setQuantity(2);

        CreateSaleRequestDTO request = new CreateSaleRequestDTO();
        request.setCustomerId(1L);
        request.setItems(List.of(itemRequest));

        Sale savedSale = new Sale();
        savedSale.setId(99L);

        SaleResponseDTO responseDTO = new SaleResponseDTO();

        when(customerService.findOrThrow(1L)).thenReturn(customer);
        when(productService.findOrThrow(10L)).thenReturn(product);
        when(repository.save(any(Sale.class))).thenReturn(savedSale);
        when(mapper.toDTO(savedSale)).thenReturn(responseDTO);

        SaleResponseDTO response = service.create(request);

        assertNotNull(response);

        verify(repository).save(any(Sale.class));

        verify(stockService).registerSaleExit(
                10L,
                99L,
                2
        );
    }

    @Test
    void shouldCalculateTotalAmountCorrectly() {

        Customer customer = new Customer();
        customer.setId(1L);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(10));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(5));

        CreateSaleItemRequestDTO item1 = new CreateSaleItemRequestDTO();
        item1.setProductId(1L);
        item1.setQuantity(2);

        CreateSaleItemRequestDTO item2 = new CreateSaleItemRequestDTO();
        item2.setProductId(2L);
        item2.setQuantity(3);

        CreateSaleRequestDTO request = new CreateSaleRequestDTO();
        request.setCustomerId(1L);
        request.setItems(List.of(item1, item2));

        when(customerService.findOrThrow(1L)).thenReturn(customer);
        when(productService.findOrThrow(1L)).thenReturn(product1);
        when(productService.findOrThrow(2L)).thenReturn(product2);

        when(repository.save(any(Sale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mapper.toDTO(any())).thenReturn(new SaleResponseDTO());

        service.create(request);

        verify(repository).save(argThat(sale ->
                sale.getTotalAmount().compareTo(BigDecimal.valueOf(35)) == 0
        ));
    }

    @Test
    void shouldCancelSaleSuccessfully() {

        Product product = new Product();
        product.setId(1L);

        SaleItem item = new SaleItem();
        item.setProduct(product);
        item.setQuantity(3);

        Sale sale = new Sale();
        sale.setId(1L);
        sale.setStatus(SaleStatus.COMPLETED);
        sale.setItems(List.of(item));

        when(repository.findById(1L))
                .thenReturn(Optional.of(sale));

        when(repository.save(any()))
                .thenReturn(sale);

        when(mapper.toDTO(any()))
                .thenReturn(new SaleResponseDTO());

        SaleResponseDTO response = service.cancel(1L);

        assertNotNull(response);

        assertEquals(SaleStatus.CANCELED, sale.getStatus());

        verify(stockService).registerEntry(
                1L,
                3,
                "Sale canceled"
        );
    }

    @Test
    void shouldThrowExceptionWhenSaleAlreadyCanceled() {

        Sale sale = new Sale();
        sale.setStatus(SaleStatus.CANCELED);

        when(repository.findById(1L))
                .thenReturn(Optional.of(sale));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cancel(1L)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundWhenSaleDoesNotExist() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.findOrThrow(1L)
        );
    }
}