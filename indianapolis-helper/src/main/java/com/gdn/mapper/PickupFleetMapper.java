package com.gdn.mapper;

import com.gdn.entity.LogisticVendor;
import com.gdn.entity.PickupFleet;
import com.gdn.entity.RecommendationFleet;
import com.gdn.request.FleetChoiceRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PickupFleetMapper {
    public static PickupFleet toPickupFleet(RecommendationFleet recommendationFleet,
                                            FleetChoiceRequest fleetChoiceRequest){
        return PickupFleet.builder()
                .id("pickup_fleet_" + UUID.randomUUID().toString())
                .fleet(recommendationFleet.getFleet())
                .logisticVendor(LogisticVendor.builder()
                        .id(fleetChoiceRequest.getLogisticVendorId())
                        .build())
                .plateNumber("plate_number_" + UUID.randomUUID().toString())
                .pickupDetailList(PickupDetailMapper.toPickupDetailList(recommendationFleet.getRecommendationDetailList()))
                .build();
    }
    public static List<PickupFleet> toPickupFleetList(List<RecommendationFleet> recommendationFleetList,
                                                      List<FleetChoiceRequest> fleetChoiceRequestList){
        List<PickupFleet> pickupFleetList = new ArrayList<>();
        Iterator<RecommendationFleet> recommendationFleetIterator = recommendationFleetList.iterator();
        Iterator<FleetChoiceRequest> fleetChoiceRequestIterator = fleetChoiceRequestList.iterator();
        while(recommendationFleetIterator.hasNext() && fleetChoiceRequestIterator.hasNext()){
            pickupFleetList.add(
                    toPickupFleet(recommendationFleetIterator.next(), fleetChoiceRequestIterator.next())
            );
        }
        return pickupFleetList;
    }
}
