package com.gdn.email;

import com.gdn.entity.*;

import java.util.List;

public interface SendEmailService {
    List<String> getTpEmailList(List<PickupDetail> pickupDetailList);
    List<String> getLogisticVendorEmailList(List<Pickup> pickupList);
    List<String> getMerchantEmailList(List<PickupDetail> pickupDetailList);
    List<Merchant> getMerchantList(List<PickupDetail> pickupDetailList);
    String getWarehouseEmail(RecommendationResult recommendationResult);
    String getWarehouseEmailContent(Warehouse warehouse);
    String getLogisticVendorEmailContent(Warehouse warehouse);
    String getMerchantEmailContent(Warehouse warehouse, Merchant merchant);
}
