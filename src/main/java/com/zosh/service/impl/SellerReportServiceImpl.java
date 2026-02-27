package com.zosh.service.impl;

import com.zosh.domain.OrderStatus;
import com.zosh.model.Order;
import com.zosh.model.Seller;
import com.zosh.model.SellerReport;
import com.zosh.repository.OrderRepository;
import com.zosh.repository.SellerReportRepository;
import com.zosh.service.SellerReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;
    private final OrderRepository orderRepository;

    public SellerReportServiceImpl(SellerReportRepository sellerReportRepository, OrderRepository orderRepository) {
        this.sellerReportRepository = sellerReportRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport report = sellerReportRepository.findBySellerId(seller.getId());
        if (report == null) {
            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport);
        }
        return report;
    }

    @Override
    public SellerReport getSellerReportByType(Seller seller, String type) {
        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        if (type == null || type.isEmpty() || type.equalsIgnoreCase("lifetime")) {
            return getSellerReport(seller);
        }

        switch (type.toUpperCase()) {
            case "TODAY":
                startDate = LocalDateTime.now().with(LocalTime.MIN);
                break;
            case "7DAYS":
                startDate = LocalDateTime.now().minusDays(7);
                break;
            case "30DAYS":
                startDate = LocalDateTime.now().minusDays(30);
                break;
            case "3MONTHS":
                startDate = LocalDateTime.now().minusMonths(3);
                break;
            case "6MONTHS":
                startDate = LocalDateTime.now().minusMonths(6);
                break;
            case "12MONTHS":
                startDate = LocalDateTime.now().minusYears(1);
                break;
            default:
                return getSellerReport(seller);
        }

        List<Order> orders = orderRepository.findBySellerIdAndOrderDateBetween(seller.getId(), startDate, endDate);

        SellerReport report = new SellerReport();
        report.setSeller(seller);

        long earnings = 0;
        long sales = 0;
        int cancelled = 0;

        for (Order order : orders) {
            if (order.getOrderStatus() != OrderStatus.CANCELLED) {
                earnings += order.getTotalSellingPrice();
                sales += order.getTotalSellingPrice();
            } else {
                cancelled++;
            }
        }

        report.setTotalEarnings(earnings);
        report.setTotalSales(sales);
        report.setCanceledOrders(cancelled);
        report.setTotalOrders(orders.size());
        report.setTotalRefunds(0L);

        return report;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
