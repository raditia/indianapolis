package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.*;
import com.gdn.helper.DateHelper;
import com.gdn.recommendation.*;
import com.gdn.recommendation.Pickup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class RecommendationResultWriter implements ItemWriter<List<Recommendation>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationResultWriter.class);

    @Autowired
    private RecommendationService recommendationService;

    @Override
    public void write(List<? extends List<Recommendation>> items) throws Exception {
        RecommendationResult recommendationResult;
        RecommendationFleet recommendationFleet;
        RecommendationDetail recommendationDetail;
        for (List<Recommendation> recommendationList:items
             ) {
            for (Recommendation recommendation:recommendationList){
                recommendationResult = buildRecommendationResult(recommendation);
                recommendationService.saveRecommendationResult(recommendationResult);
                for (Pickup pickup:recommendation.getPickupList()){
                    recommendationFleet = buildRecommendationFleet(recommendationResult, pickup);
                    recommendationService.saveRecommendationFleet(recommendationFleet);
                    for (Detail detail:pickup.getDetailList()){
                        recommendationDetail = buildRecommendationDetail(recommendationFleet, detail);
                        recommendationService.saveRecommendationDetail(recommendationDetail);
                    }
                }
            }
        }
    }

    private RecommendationResult buildRecommendationResult(Recommendation recommendation){
        return RecommendationResult.builder()
                .id(recommendation.getId())
                .pickupDate(DateHelper.tomorrow())
                .totalSku(recommendation.getSkuAmount())
                .totalCbm(recommendation.getCbmTotal())
                .warehouse(Warehouse.builder()
                        .id(recommendation.getWarehouseId())
                        .build())
                .build();
    }

    private RecommendationFleet buildRecommendationFleet(RecommendationResult recommendationResult,
                                                         Pickup pickup){
        return RecommendationFleet.builder()
                .id("recommendation_fleet_" + UUID.randomUUID().toString())
                .recommendationResult(recommendationResult)
                .fleet(pickup.getFleet())
                .fleetSkuPickupQty(pickup.getPickupTotalAmount())
                .fleetCbmPickupAmount(pickup.getPickupTotalCbm())
                .build();
    }

    private RecommendationDetail buildRecommendationDetail(RecommendationFleet recommendationFleet,
                                                           Detail detail){
        return RecommendationDetail.builder()
                .id("recommendation_detail_" + UUID.randomUUID().toString())
                .recommendationFleet(recommendationFleet)
                .sku(CffGood.builder()
                        .id(detail.getSku().getId())
                        .build())
                .skuPickupQty(detail.getPickupAmount())
                .cbmPickupAmount(detail.getCbmPickup())
                .merchant(Merchant.builder()
                        .id(detail.getSku().getMerchantId())
                        .build())
                .pickupPoint(PickupPoint.builder()
                        .id(detail.getSku().getPickupPointId())
                        .build())
                .build();
    }

}
