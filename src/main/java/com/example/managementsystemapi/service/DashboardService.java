package com.example.managementsystemapi.service;

import com.example.managementsystemapi.dto.dashboard.*;
import com.example.managementsystemapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final SaleRepository saleRepository;

    private final SaleItemRepository saleItemRepository;

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getSummary() {

        log.info("Fetching dashboard summary");

        long totalCustomers = customerRepository.countByActiveIsTrue();
        long totalProducts = productRepository.countByActiveIsTrue();

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        long salesToday = saleRepository.countSalesBetweenDates(startOfDay, endOfDay);

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        BigDecimal currentMonthRevenue = saleRepository.sumRevenueBetweenDates(startOfMonth, endOfMonth);

        return new DashboardSummaryDTO(totalCustomers, totalProducts, salesToday, currentMonthRevenue);
    }

    @Transactional(readOnly = true)
    public List<SalesByDayDTO> getSalesByDay() {

        log.info("Fetching sales by day - period: last 30 days");

        LocalDateTime startDate = LocalDate.now().minusDays(30).atStartOfDay();

        return saleRepository.findSalesByDay(startDate);
    }

    @Transactional(readOnly = true)
    public List<TopProductDTO> getTopProducts() {

        log.info("Fetching top products - period: last 30 days, limit: 5");

        LocalDateTime startDate = LocalDate.now().minusDays(30).atStartOfDay();

        return saleItemRepository.findTopProducts(startDate, PageRequest.of(0, 5));
    }

    @Transactional(readOnly = true)
    public List<RecentSaleDTO> getRecentSales() {

        log.info("Fetching recent sales - limit: 3");

        return saleRepository.findRecentSales(PageRequest.of(0, 3));
    }

    @Transactional(readOnly = true)
    public List<TopCustomerDTO> getTopCustomers() {

        log.info("Fetching top customers - period: last 30 days, limit: 3");

        LocalDateTime startDate = LocalDate.now().minusDays(30).atStartOfDay();

        return saleRepository.findTopCustomers(startDate, PageRequest.of(0, 3));
    }

    @Transactional(readOnly = true)
    public StockAlertDTO getStockAlert() {

        log.info("Fetching stock alert");

        return new StockAlertDTO(stockRepository.countLowStock());
    }
}
