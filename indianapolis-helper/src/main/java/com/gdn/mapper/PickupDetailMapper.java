package com.gdn.mapper;

import com.gdn.entity.PickupDetail;
import com.gdn.entity.RecommendationDetail;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PickupDetailMapper {

    public static PickupDetail toPickupDetail(RecommendationDetail recommendationDetail){
        return PickupDetail.builder()
                .id("pickup_detail_" + UUID.randomUUID().toString())
                .cbmPickupAmount(recommendationDetail.getCbmPickupAmount())
                .skuPickupQuantity(recommendationDetail.getSkuPickupQty())
                .merchant(recommendationDetail.getMerchant())
                .sku(recommendationDetail.getSku())
                .pickupPoint(recommendationDetail.getPickupPoint())
                .build();
    }

    public static List<PickupDetail> toPickupDetailList(List<RecommendationDetail> recommendationDetailList){
        return recommendationDetailList.stream()
                .map(PickupDetailMapper::toPickupDetail)
                .collect(Collectors.toList());
    }

}
