package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.dto.dashboard.*;
import com.example.managementsystemapi.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dashboard", description = "Operations related to dashboard data and statistics")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get dashboard summary", description = "Returns the main dashboard statistics, including active customers, active products, sales today and current month revenue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard summary retrieved successfully")
    })
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @Operation(summary = "Get sales by day", description = "Returns daily sales data for the last 30 days")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales data retrieved successfully")
    })
    @GetMapping("/sales-by-day")
    public ResponseEntity<List<SalesByDayDTO>> getSalesByDay() {
        return ResponseEntity.ok(dashboardService.getSalesByDay());
    }

    @Operation(summary = "Get top products", description = "Returns the five best-selling products based on sales from the last 30 days")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top products retrieved successfully")
    })
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDTO>> getTopProducts() {

        return ResponseEntity.ok(dashboardService.getTopProducts());
    }

    @Operation(summary = "Get recent sales", description = "Returns the three most recent sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recent sales retrieved successfully")
    })
    @GetMapping("/recent-sales")
    public ResponseEntity<List<RecentSaleDTO>> getRecentSales() {

        return ResponseEntity.ok(dashboardService.getRecentSales());
    }

    @Operation(summary = "Get top customers", description = "Returns the three customers with the highest sales volume based on sales from the last 30 days")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top customers retrieved successfully")
    })
    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerDTO>> getTopCustomers() {

        return ResponseEntity.ok(dashboardService.getTopCustomers());
    }

    @Operation(summary = "Get stock alert", description = "Returns the number of products currently below the minimum stock level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock alert retrieved successfully")
    })
    @GetMapping("/stock-alert")
    public ResponseEntity<StockAlertDTO> getStockAlert() {

        return ResponseEntity.ok(dashboardService.getStockAlert());
    }
}
