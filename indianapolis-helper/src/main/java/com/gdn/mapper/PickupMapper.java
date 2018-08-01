package com.gdn.mapper;

import com.gdn.entity.Pickup;
import com.gdn.entity.RecommendationResult;

import java.util.UUID;

public class PickupMapper {
    public static Pickup toPickup(RecommendationResult recommendationResult){
        return Pickup.builder()
                .id("pickup_" + UUID.randomUUID().toString())
                .pickupDate(recommendationResult.getPickupDate())
                .warehouse(recommendationResult.getWarehouse())
                .pickupFleetList(PickupFleetMapper.toPickupFleetList(recommendationResult.getRecommendationFleetList()))
                .build();
    }
}
