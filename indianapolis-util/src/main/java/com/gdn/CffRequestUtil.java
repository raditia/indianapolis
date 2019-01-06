package com.gdn;

import com.gdn.request.CffRequest;

public class CffRequestUtil {
    public static CffRequest cffRequestCompleteAttributeExistingMerchantExistingPickupPoint = CffRequest.builder()
            .id(CffUtil.cffCompleteAttribute.getId())
            .tp(TradePartnershipRequestUtil.tradePartnershipRequestCompleteAttribute)
            .merchant(MerchantRequestUtil.existingMerchantRequestCompleteAttribute)
            .warehouse(WarehouseRequestUtil.warehouseRequestCompleteAttribute)
            .cffGoodList(CffGoodRequestUtil.cffGoodRequestListCompleteAttribute)
            .pickupPoint(PickupPointRequestUtil.existingPickupPointRequestCompleteAttribute)
            .build();
    public static CffRequest cffRequestCompleteAttributeNewMerchantNewPickupPoint = CffRequest.builder()
            .id(CffUtil.cffCompleteAttribute.getId())
            .tp(TradePartnershipRequestUtil.tradePartnershipRequestCompleteAttribute)
            .merchant(MerchantRequestUtil.newMerchantRequestCompleteAttribute)
            .warehouse(WarehouseRequestUtil.warehouseRequestCompleteAttribute)
            .cffGoodList(CffGoodRequestUtil.cffGoodRequestListCompleteAttribute)
            .pickupPoint(PickupPointRequestUtil.newPickupPointCompleteAttribute)
            .build();
}
