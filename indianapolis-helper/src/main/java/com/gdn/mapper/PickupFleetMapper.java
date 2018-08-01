package com.gdn.mapper;

import com.gdn.entity.PickupFleet;
import com.gdn.entity.RecommendationFleet;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PickupFleetMapper {
    public static PickupFleet toPickupFleet(RecommendationFleet recommendationFleet){
        return PickupFleet.builder()
                .id("pickup_fleet_" + UUID.randomUUID().toString())
                .fleet(recommendationFleet.getFleet())
                .plateNumber("plate_number_" + UUID.randomUUID().toString())
                .pickupDetailList(PickupDetailMapper.toPickupDetailList(recommendationFleet.getRecommendationDetailList()))
                .build();
    }
    public static List<PickupFleet> toPickupFleetList(List<RecommendationFleet> recommendationFleetList){
        return recommendationFleetList.stream()
                .map(PickupFleetMapper::toPickupFleet)
                .collect(Collectors.toList());
    }
}
