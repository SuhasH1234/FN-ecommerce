package com.zosh.service;

import com.zosh.model.Seller;
import com.zosh.model.SellerReport;
import java.util.List;
import java.util.Optional;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);

    SellerReport getSellerReportByType(Seller seller, String type);

    SellerReport updateSellerReport(SellerReport sellerReport);

}
