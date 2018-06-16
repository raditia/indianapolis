package com.gdn.email;

import com.gdn.entity.Pickup;
import com.gdn.entity.PickupDetail;
import com.gdn.entity.RecommendationResult;

import java.util.List;

public interface SendEmailService {
    List<String> getTpEmailList(List<PickupDetail> pickupDetailList);
    List<String> getLogisticVendorEmailList(List<Pickup> pickupList);
    List<String> getMerchantEmailList(List<PickupDetail> pickupDetailList);
    String getWarehouseEmail(RecommendationResult recommendationResult);
}
