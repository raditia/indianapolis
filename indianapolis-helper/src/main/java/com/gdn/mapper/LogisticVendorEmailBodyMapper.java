package com.gdn.mapper;

import com.gdn.email.LogisticVendorEmailBody;
import com.gdn.email.SkuPickup;
import com.gdn.entity.Pickup;
import com.gdn.entity.PickupDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogisticVendorEmailBodyMapper {
    public static LogisticVendorEmailBody toLogisticVendorEmailBody(Pickup pickup){
        LogisticVendorEmailBody logisticVendorEmailBody;
        Map<String, String> pickupIdAndMerchantNameMap = new HashMap<>();
        List<SkuPickup> skuPickupList = new ArrayList<>();
        logisticVendorEmailBody = LogisticVendorEmailBody.builder()
                .fleetName(pickup.getFleet().getName())
                .warehouseDestinationName(pickup.getWarehouse().getAddress())
                .warehouseDestinationPhoneNumber(pickup.getWarehouse().getPhoneNumber())
                .build();
        for (PickupDetail pickupDetail:pickup.getPickupDetailList()){
            if(!pickupIdAndMerchantNameMap.containsKey(pickup.getId())
                    && !pickupIdAndMerchantNameMap.containsValue(pickupDetail.getMerchant().getName())){
                logisticVendorEmailBody.setMerchantName(pickupDetail.getMerchant().getName());
                logisticVendorEmailBody.setMerchantPhoneNumber(pickupDetail.getMerchant().getPhoneNumber());
                logisticVendorEmailBody.setMerchantAddress(pickupDetail.getPickupPoint().getPickupAddress());
                logisticVendorEmailBody.setMerchantAddressCoordinate(
                        String.valueOf(pickupDetail.getPickupPoint().getLatitude()) + "," + String.valueOf(pickupDetail.getPickupPoint().getLongitude()));
            }
            SkuPickup skuPickup = SkuPickup.builder()
                    .sku(pickupDetail.getSku().getSku())
                    .skuPickupQuantity(pickupDetail.getSkuPickupQuantity())
                    .skuPickupCbmAmount(pickupDetail.getCbmPickupAmount())
                    .build();
            skuPickupList.add(skuPickup);
        }
        logisticVendorEmailBody.setSkuPickupList(skuPickupList);
        return logisticVendorEmailBody;
    }
    public static List<LogisticVendorEmailBody> toLogisticVendorEmailBodyList(List<Pickup> pickupList){
        return pickupList.stream()
                .map(LogisticVendorEmailBodyMapper::toLogisticVendorEmailBody)
                .collect(Collectors.toList());
    }
}
