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
//        RecommendationResult recommendationResult;
//        List<RecommendationFleet> recommendationFleetList = new ArrayList<>();
//        List<RecommendationDetail> recommendationDetailList = new ArrayList<>();
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
//            for (Recommendation recommendation:recommendationList
//                 ) {
//                recommendationResult = RecommendationResult.builder()
//                        .id("recommendation_result_" + UUID.randomUUID().toString())
//                        .totalSku(recommendation.getSkuAmount())
//                        .totalCbm(recommendation.getCbmTotal())
//                        .pickupDate(tomorrow())
//                        .warehouse(Warehouse.builder()
//                                .id(recommendation.getWarehouseId())
//                                .build())
//                        .build();
////                System.out.println("\n" + "HASIL REKOMENDASI!");
////                System.out.println("ID : " + recommendation.getId() + "\n" +
////                        "TOTAL SKU : " + recommendation.getSkuAmount() + "\n" +
////                        "TOTAL CBM : " + recommendation.getCbmTotal() + "\n" +
////                        "WAREHOUSE : " + recommendation.getWarehouseId());
//                for (Pickup pickup:recommendation.getPickupList()
//                     ) {
//                    recommendationFleetList.add(RecommendationFleet.builder()
//                            .id("recommendation_fleet_" + UUID.randomUUID().toString())
//                            .fleetSkuPickupQty(pickup.getPickupTotalAmount())
//                            .fleetCbmPickupAmount(pickup.getPickupTotalCbm())
//                            .build());
//                    recommendationResult.setRecommendationFleetList(recommendationFleetList);
////                    System.out.println("\n" +
////                            "FLEET : " + pickup.getFleet().getName() + "\n" +
////                            "FLEET SKU PICKUP : " + pickup.getPickupTotalAmount() + "\n" +
////                            "FLEET CBM PICKUP : " + pickup.getPickupTotalCbm());
//                    for (Detail detail:pickup.getDetailList()
//                         ) {
//                        recommendationDetailList.add(RecommendationDetail.builder()
//                                .id("recommendation_detail_" + UUID.randomUUID().toString())
//                                .merchant(Merchant.builder()
//                                        .id(detail.getSku().getMerchantId())
//                                        .build())
//                                .sku(detail.getSku().getId())
//                                .skuPickupQty(detail.getPickupAmount())
//                                .cbmPickupAmount(detail.getCbmPickup())
//                                .pickupPoint(PickupPoint.builder()
//                                        .id(detail.getSku().getPickupPointId())
//                                        .build())
//                                .build());
////                        System.out.println("SKU DETAIL : " + detail.getSku().getName() + "\n" +
////                                "SKU PICKUP : " + detail.getPickupAmount() + "\n" +
////                                "CBM PICKUP : " + detail.getCbmPickup() + "\n" +
////                                "MERCHANT ID : " + detail.getSku().getMerchantId() + "\n" +
////                                "PICKUP POINT ID : " + detail.getSku().getPickupPointId());
////                        recommendationService.saveRecommendationDetail(RecommendationDetail.builder()
////                                .recommendationFleet(RecommendationFleet.builder()
////                                        .recommendationResult(RecommendationResult.builder()
////                                                .id("recommendation_result_" + UUID.randomUUID().toString())
////                                                .warehouse(Warehouse.builder()
////                                                        .id(recommendation.getWarehouseId())
////                                                        .build())
////                                                .pickupDate(tomorrow())
////                                                .totalCbm(recommendation.getCbmTotal())
////                                                .totalSku(recommendation.getSkuAmount())
////                                                .build())
////                                        .id("recommendation_fleet_" + UUID.randomUUID().toString())
////                                        .fleet(Fleet.builder()
////                                                .id(pickup.getFleet().getId())
////                                                .build())
////                                        .fleetCbmPickupAmount(pickup.getPickupTotalCbm())
////                                        .fleetSkuPickupQty(pickup.getPickupTotalAmount())
////                                        .build())
////                                .id("recommendation_detail_" + UUID.randomUUID().toString())
////                                .sku(detail.getSku().getName())
////                                .pickupPoint(PickupPoint.builder()
////                                        .id(detail.getSku().getPickupPointId())
////                                        .build())
////                                .cbmPickupAmount(detail.getCbmPickup())
////                                .skuPickupQty(detail.getPickupAmount())
////                                .merchant(Merchant.builder()
////                                        .id(detail.getSku().getMerchantId())
////                                        .build())
////                                .build());
//                    }
//                }
//                recommendationService.saveRecommendationResult(recommendationResult);
//            }
        }
    }

    private RecommendationResult buildRecommendationResult(Recommendation recommendation){
        return RecommendationResult.builder()
                .id(recommendation.getId())
                .pickupDate(tomorrow())
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

    private Date tomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

}
