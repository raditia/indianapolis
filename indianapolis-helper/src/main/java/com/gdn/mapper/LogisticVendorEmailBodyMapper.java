package com.gdn.mapper;

import com.gdn.email.LogisticVendorEmailBody;
import com.gdn.email.SkuPickup;
import com.gdn.entity.PickupFleet;
import com.gdn.entity.PickupDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogisticVendorEmailBodyMapper {
    public static LogisticVendorEmailBody toLogisticVendorEmailBody(PickupFleet pickupFleet){
        LogisticVendorEmailBody logisticVendorEmailBody;
        Map<String, String> pickupIdAndMerchantNameMap = new HashMap<>();
        List<SkuPickup> skuPickupList = new ArrayList<>();
        logisticVendorEmailBody = LogisticVendorEmailBody.builder()
                .fleetName(pickupFleet.getFleet().getName())
                .warehouseDestinationName(pickupFleet.getPickup().getWarehouse().getAddress())
                .warehouseDestinationPhoneNumber(pickupFleet.getPickup().getWarehouse().getPhoneNumber())
                .build();
        for (PickupDetail pickupDetail: pickupFleet.getPickupDetailList()){
            if(!pickupIdAndMerchantNameMap.containsKey(pickupFleet.getId())
                    && !pickupIdAndMerchantNameMap.containsValue(pickupDetail.getMerchant().getName())){
                pickupIdAndMerchantNameMap.put(pickupFleet.getId(), pickupDetail.getMerchant().getName());
                logisticVendorEmailBody.setMerchantName(pickupDetail.getMerchant().getName());
                logisticVendorEmailBody.setMerchantPhoneNumber(pickupDetail.getMerchant().getPhoneNumber());
                logisticVendorEmailBody.setMerchantAddress(pickupDetail.getPickupPoint().getPickupAddress());
                logisticVendorEmailBody.setMerchantAddressCoordinate(
                        String.valueOf(pickupDetail.getPickupPoint().getLatitude()) + "," + String.valueOf(pickupDetail.getPickupPoint().getLongitude()));
            }
            SkuPickup skuPickup = SkuPickup.builder()
                    .sku(pickupDetail.getCffGood().getSku())
                    .skuPickupQuantity(pickupDetail.getSkuPickupQuantity())
                    .skuPickupCbmAmount(pickupDetail.getCbmPickupAmount())
                    .build();
            skuPickupList.add(skuPickup);
        }
        logisticVendorEmailBody.setSkuPickupList(skuPickupList);
        return logisticVendorEmailBody;
    }
    public static List<LogisticVendorEmailBody> toLogisticVendorEmailBodyList(List<PickupFleet> pickupFleetList){
        return pickupFleetList.stream()
                .map(LogisticVendorEmailBodyMapper::toLogisticVendorEmailBody)
                .collect(Collectors.toList());
    }
}
