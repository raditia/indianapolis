package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.*;
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
//            for (Recommendation recommendation:recommendationList){
//                System.out.println("Rekomendasi ID : " + recommendation.getId());
//                System.out.println("Pickup List size : " + recommendation.getPickupList().size());
//                for (Pickup pickup:recommendation.getPickupList()){
//                    System.out.println("Fleet : " + pickup.getFleet().getName());
//                    recommendationService.saveRecommendationFleet(buildRecommendationFleet(recommendation, pickup));
//                }
//            }
//            for (Recommendation recommendation:recommendationList){
//                for (Pickup pickup:recommendation.getPickupList()){
//                    for (Detail detail:pickup.getDetailList()){
//                        recommendationService.saveRecommendationDetail(
//                                buildRecommendationDetail(recommendation, pickup, detail));
//                    }
//                }
//            }
            for (Recommendation recommendation:recommendationList
                 ) {
                System.out.println("\n" + "HASIL REKOMENDASI!");
                System.out.println("ID : " + recommendation.getId() + "\n" +
                        "TOTAL SKU : " + recommendation.getSkuAmount() + "\n" +
                        "TOTAL CBM : " + recommendation.getCbmTotal());
                for (Pickup pickup:recommendation.getPickupList()
                     ) {
                    System.out.println("\n" +
                            "FLEET : " + pickup.getFleet().getName() + "\n" +
                            "FLEET SKU PICKUP : " + pickup.getPickupTotalAmount() + "\n" +
                            "FLEET CBM PICKUP : " + pickup.getPickupTotalCbm());
                    for (Detail detail:pickup.getDetailList()
                         ) {
                        System.out.println("SKU DETAIL : " + detail.getSku().getName() + "\n" +
                                "SKU PICKUP : " + detail.getPickupAmount() + "\n" +
                                "CBM PICKUP : " + detail.getCbmPickup() + "\n" +
                                "WAREHOUSE TUJUAN : " + detail.getSku().getWarehouseId() + "\n" +
                                "MERCHANT ID : " + detail.getSku().getMerchantId() + "\n" +
                                "PICKUP POINT ID : " + detail.getSku().getPickupPointId());
                    }
                }
            }
        }
    }

    private RecommendationResult buildRecommendationResult(Recommendation recommendation){
        return RecommendationResult.builder()
                .id(recommendation.getId())
                .pickupDate(tomorrow())
                .totalSku(recommendation.getSkuAmount())
                .totalCbm(recommendation.getCbmTotal())
                .build();
    }

    private RecommendationFleet buildRecommendationFleet(RecommendationResult recommendationResult,
                                                         Pickup pickup){
        return RecommendationFleet.builder()
                .id(UUID.randomUUID().toString())
                .recommendationResult(recommendationResult)
                .fleet(pickup.getFleet())
                .fleetSkuPickupQty(pickup.getPickupTotalAmount())
                .fleetCbmPickupAmount(pickup.getPickupTotalCbm())
                .build();
    }

    private RecommendationDetail buildRecommendationDetail(RecommendationFleet recommendationFleet,
                                                           Detail detail){
        return RecommendationDetail.builder()
                .id(UUID.randomUUID().toString())
                .recommendationFleet(recommendationFleet)
                .sku(detail.getSku().getName())
                .skuPickupQty(detail.getPickupAmount())
                .cbmPickupAmount(detail.getCbmPickup())
                .warehouse(Warehouse.builder()
                        .id(detail.getSku().getWarehouseId())
                        .build())
                .merchant(Merchant.builder()
                        .id(detail.getSku().getMerchantId())
                        .build())
                .pickupPoint(PickupPoint.builder()
                        .id(detail.getSku().getPickupPointId())
                        .build())
                .build();
    }

    private Date tomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

}
