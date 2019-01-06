package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.*;
import com.gdn.helper.DateHelper;
import com.gdn.recommendation.*;
import com.gdn.recommendation.Pickup;
import com.gdn.repository.RecommendationResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class FleetRecommendationWriter implements ItemWriter<List<Recommendation>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleetRecommendationWriter.class);

    @Autowired
    private RecommendationResultRepository recommendationResultRepository;

    @Override
    public void write(List<? extends List<Recommendation>> items) throws Exception {
        for (List<Recommendation> recommendationList:items){
            for (Recommendation recommendation:recommendationList){
                List<RecommendationFleet> recommendationFleetList = populateRecommendationFleetList(recommendation);
                RecommendationResult recommendationResult = buildRecommendationResult(recommendation, recommendationFleetList);
                for (RecommendationFleet recommendationFleet:recommendationResult.getRecommendationFleetList()){
                    recommendationFleet.setRecommendationResult(recommendationResult);
                    for (RecommendationDetail recommendationDetail:recommendationFleet.getRecommendationDetailList()){
                        recommendationDetail.setRecommendationFleet(recommendationFleet);
                    }
                }
                recommendationResultRepository.save(recommendationResult);
            }
        }
    }

    private List<RecommendationFleet> populateRecommendationFleetList(Recommendation recommendation){
        List<RecommendationFleet> recommendationFleetList = new ArrayList<>();
        for (Pickup pickup:recommendation.getPickupList()){
            List<RecommendationDetail> recommendationDetailList = populateRecommendationDetailList(pickup);
            RecommendationFleet recommendationFleet = buildRecommendationFleet(pickup, recommendationDetailList);
            recommendationFleetList.add(recommendationFleet);
        }
        return recommendationFleetList;
    }

    private List<RecommendationDetail> populateRecommendationDetailList(Pickup pickup){
        List<RecommendationDetail> recommendationDetailList = new ArrayList<>();
        for (DetailPickup detail:pickup.getDetailPickupList()){
            RecommendationDetail recommendationDetail = buildRecommendationDetail(detail);
            recommendationDetailList.add(recommendationDetail);
        }
        return recommendationDetailList;
    }

    private RecommendationDetail buildRecommendationDetail(DetailPickup detail){
        return RecommendationDetail.builder()
                .id("recommendation_detail_" + UUID.randomUUID().toString())
                .cffGood(CffGood.builder()
                        .id(detail.getProduct().getId())
                        .build())
                .skuPickupQty(detail.getPickupAmount())
                .cbmPickupAmount(detail.getPickupCbm())
                .merchant(Merchant.builder()
                        .id(detail.getProduct().getMerchantId())
                        .build())
                .pickupPoint(PickupPoint.builder()
                        .id(detail.getProduct().getPickupPointId())
                        .build())
                .build();
    }

    private RecommendationFleet buildRecommendationFleet(Pickup pickup,
                                                         List<RecommendationDetail> recommendationDetailList){
        return RecommendationFleet.builder()
                .id("recommendation_fleet_" + UUID.randomUUID().toString())
                .fleet(pickup.getFleet())
                .fleetSkuPickupQty(pickup.getPickupTotalAmount())
                .fleetCbmPickupAmount(pickup.getPickupTotalCbm())
                .recommendationDetailList(recommendationDetailList)
                .build();
    }

    private RecommendationResult buildRecommendationResult(Recommendation recommendation,
                                                           List<RecommendationFleet> recommendationFleetList){
        return RecommendationResult.builder()
                .id(recommendation.getId())
                .pickupDate(DateHelper.setDay(2))
                .totalSku(recommendation.getProductAmount())
                .totalCbm(recommendation.getCbmTotal())
                .warehouse(Warehouse.builder()
                        .id(recommendation.getWarehouseId())
                        .build())
                .recommendationFleetList(recommendationFleetList)
                .build();
    }

}
