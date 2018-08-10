package com.gdn.recommendation_algorithm.implementation;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.*;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import com.gdn.recommendation_algorithm.Helper;
import com.gdn.recommendation_algorithm.PickupProcessorService;
import com.gdn.recommendation_algorithm.RecommendationProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.soap.Detail;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationProcessorImpl implements RecommendationProcessorService {

    @Autowired
    private FleetProcessorService fleetProcessorService;
    @Autowired
    private PickupProcessorService pickupProcessorService;

    @Override
    public List<Recommendation> getThreeRecommendation(List<DatabaseQueryResult> productResultList, String warehouseId){
        List<Recommendation> threeRecommendations = new ArrayList<>();
        List<Product> productList;

        List<Fleet> topThreeFleetsWillUsed = fleetProcessorService.getTopThreeFleetsWillUsed(productResultList);
        for(Fleet topFleetWillUsed : topThreeFleetsWillUsed){
            productList = Helper.migrateIntoProductList(productResultList);
            Recommendation recommendation = getRecommendationByTopFleet(productList, topFleetWillUsed, warehouseId);
            threeRecommendations.add(recommendation);
        }

        return threeRecommendations;
    }

    private Recommendation getRecommendationByTopFleet(List<Product> productList, Fleet topFleetWillUsed, String warehouseId){
        List<Pickup> pickupList = new ArrayList<>();
        Float cbmTotal = 0.0f;
        Integer productAmount = 0;
        Fleet fleetWithMaxCbmCapacityWillUsed = topFleetWillUsed;

        while(!Helper.empty(productList)){
            Pickup nextPickup = pickupProcessorService.getNextPickup(productList, topFleetWillUsed);
            if(nextPickup.getPickupTotalAmount() > 0) {
                pickupList.add(nextPickup);
                cbmTotal = Helper.formatNormalFloat(cbmTotal+nextPickup.getPickupTotalCbm());
                productAmount += nextPickup.getPickupTotalAmount();
                topFleetWillUsed = fleetWithMaxCbmCapacityWillUsed;
            } else {
                topFleetWillUsed = fleetProcessorService.getFleetWithMoreCbmCapacity(topFleetWillUsed);
            }
        }

        return Recommendation.builder()
                .id("recommendation_result_" + UUID.randomUUID().toString())
                .pickupList(pickupList)
                .cbmTotal(cbmTotal)
                .productAmount(productAmount)
                .warehouseId(warehouseId)
                .build();
    }
}
