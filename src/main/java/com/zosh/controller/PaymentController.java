package com.zosh.controller;

import com.zosh.domain.PaymentMethod;
import com.zosh.model.User;
import com.zosh.model.PaymentOrder;
import com.zosh.model.Order;
import com.zosh.model.Seller;
import com.zosh.model.SellerReport;
import com.zosh.model.Cart;
import com.zosh.repository.CartItemRepository;
import com.zosh.repository.CartRepository;
import com.zosh.repository.PaymentOrderRepository;
import com.zosh.response.ApiResponse;
import com.zosh.response.PaymentLinkResponse;
import com.zosh.service.UserService;
import com.zosh.service.PaymentService;
import com.zosh.service.TransactionService;
import com.zosh.service.SellerReportService;
import com.zosh.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentOrderRepository paymentOrderRepository;

    public PaymentController(UserService userService, PaymentService paymentService,
            TransactionService transactionService, SellerReportService sellerReportService, SellerService sellerService,
            CartRepository cartRepository, CartItemRepository cartItemRepository,
            PaymentOrderRepository paymentOrderRepository) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.transactionService = transactionService;
        this.sellerReportService = sellerReportService;
        this.sellerService = sellerService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentOrderRepository = paymentOrderRepository;
    }

    @PostMapping("/api/payment/{paymentMethod}/order/{orderId}")
    public ResponseEntity<PaymentLinkResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentLinkResponse paymentResponse = new PaymentLinkResponse();

        PaymentOrder order = paymentService.getPaymentOrderById(orderId);

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            com.razorpay.PaymentLink payment = paymentService.createRazorpayPaymentLink(user,
                    order.getAmount(),
                    order.getId());
            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            paymentResponse.setPayment_link_url(paymentUrl);
            paymentResponse.setPayment_link_id(paymentUrlId);
            order.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(order);
        } else {
            String paymentUrl = paymentService.createStripePaymentLink(user,
                    order.getAmount(),
                    order.getId());
            paymentResponse.setPayment_link_url(paymentUrl);
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/payment/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentOrder paymentOrder = paymentService
                .getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId,
                paymentLinkId);
        if (paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
            Cart cart = cartRepository.findByUserId(user.getId());
            if (cart != null) {
                cart.setCouponPrice(0);
                cart.setCouponCode(null);
                cartRepository.save(cart);
            }
        }

        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
