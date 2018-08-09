package com.gdn.mapper;

import com.gdn.entity.Pickup;
import com.gdn.entity.RecommendationResult;
import com.gdn.request.FleetChoiceRequest;

import java.util.List;
import java.util.UUID;

public class PickupMapper {
    public static Pickup toPickup(RecommendationResult recommendationResult,
                                  List<FleetChoiceRequest> fleetChoiceRequestList){
        return Pickup.builder()
                .id("pickup_" + UUID.randomUUID().toString())
                .pickupDate(recommendationResult.getPickupDate())
                .warehouse(recommendationResult.getWarehouse())
                .pickupFleetList(PickupFleetMapper.toPickupFleetList(recommendationResult.getRecommendationFleetList(), fleetChoiceRequestList))
                .build();
    }
}
