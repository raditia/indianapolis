package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.*;
import com.gdn.helper.DateHelper;
import com.gdn.laff.Box;
import com.gdn.laff.BoxItem;
import com.gdn.laff.Container;
import com.gdn.laff.LargestAreaFitFirstPackager;
import com.gdn.recommendation.DetailPickup;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Recommendation;
import com.gdn.repository.RecommendationResultRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FleetRecommendationWriter implements ItemWriter<List<Recommendation>> {
    // maximum number of containers which can be used
    private static final int MAX_CONTAINERS = 1000;
    // limit search using 50 seconds deadline
    private static final double DEADLINE = System.currentTimeMillis() + 50000;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Autowired
    private RecommendationResultRepository recommendationResultRepository;

    @Autowired
    private LargestAreaFitFirstPackager largestAreaFitFirstPackager;

    @Override
    public void write(List<? extends List<Recommendation>> items) throws Exception {
        for (List<Recommendation> recommendationList : items) {
            for (Recommendation recommendation : recommendationList) {
                saveRecommendationResultToDatabase(recommendation);
            }
        }
    }

    private void saveRecommendationResultToDatabase(Recommendation recommendation) {
        List<Container> containers = new ArrayList<>();
        List<BoxItem> products = new ArrayList<>();

        List<RecommendationFleet> recommendationFleetList = populateRecommendationFleetList(recommendation);
        RecommendationResult recommendationResult = buildRecommendationResult(recommendation, recommendationFleetList);
        for (RecommendationFleet recommendationFleet : recommendationResult.getRecommendationFleetList()) {
            containers.clear();
            products.clear();
            Container container = buildContainer(recommendationFleet.getFleet());
            containers.add(container);

            recommendationFleet.setRecommendationResult(recommendationResult);

            for (RecommendationDetail recommendationDetail : recommendationFleet.getRecommendationDetailList()) {
                BoxItem boxItem = buildBoxItem(recommendationDetail.getCffGood(), recommendationDetail.getSkuPickupQty());
                products.add(boxItem);

                recommendationDetail.setRecommendationFleet(recommendationFleet);
            }
            executeLaffAlgorithm(containers, products, recommendationFleet.getFleet());
            recommendationResultRepository.save(recommendationResult);
        }
    }

    // kendaraannya apa dan dia ambil apa aja
    private List<RecommendationFleet> populateRecommendationFleetList(Recommendation recommendation) {
        List<RecommendationFleet> recommendationFleetList = new ArrayList<>();
        for (Pickup pickup : recommendation.getPickupList()) {
            List<RecommendationDetail> recommendationDetailList = populateRecommendationDetailList(pickup);
            RecommendationFleet recommendationFleet = buildRecommendationFleet(pickup, recommendationDetailList);
            recommendationFleetList.add(recommendationFleet);
        }
        return recommendationFleetList;
    }

    private List<RecommendationDetail> populateRecommendationDetailList(Pickup pickup) {
        List<RecommendationDetail> recommendationDetailList = new ArrayList<>();
        for (DetailPickup detail : pickup.getDetailPickupList()) {
            RecommendationDetail recommendationDetail = buildRecommendationDetail(detail);
            recommendationDetailList.add(recommendationDetail);
        }
        return recommendationDetailList;
    }

    private RecommendationDetail buildRecommendationDetail(DetailPickup detail) {
        return RecommendationDetail.builder()
                .id("recommendation_detail_" + UUID.randomUUID().toString())
                .cffGood(CffGood.builder()
                        .id(detail.getProduct().getId())
                        .sku(detail.getProduct().getName())
                        .width((float) detail.getProduct().getWidth())
                        .length((float) detail.getProduct().getLength())
                        .height((float) detail.getProduct().getHeight())
                        .weight((float) detail.getProduct().getWeight())
                        .quantity(detail.getProduct().getQuantityForLaff())
                        .cbm(detail.getPickupCbm())
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
                                                         List<RecommendationDetail> recommendationDetailList) {
        return RecommendationFleet.builder()
                .id("recommendation_fleet_" + UUID.randomUUID().toString())
                .fleet(pickup.getFleet())
                .fleetSkuPickupQty(pickup.getPickupTotalAmount())
                .fleetCbmPickupAmount(pickup.getPickupTotalCbm())
                .recommendationDetailList(recommendationDetailList)
                .build();
    }

    private RecommendationResult buildRecommendationResult(Recommendation recommendation,
                                                           List<RecommendationFleet> recommendationFleetList) {
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

    private Container buildContainer(Fleet fleet) {
        return new Container(
                fleet.getName(),
                fleet.getWidth(),
                fleet.getLength(),
                fleet.getHeight(),
                fleet.getWeight());
    }

    private BoxItem buildBoxItem(CffGood cffGood, int cffGoodPickupQuantity) {
        return new BoxItem(buildBox(cffGood), cffGoodPickupQuantity);
    }

    private Box buildBox(CffGood cffGood) {
        return new Box(
                cffGood.getSku(),
                Double.parseDouble(decimalFormat.format(cffGood.getWidth())),
                Double.parseDouble(decimalFormat.format(cffGood.getLength())),
                Double.parseDouble(decimalFormat.format(cffGood.getHeight())),
                Double.parseDouble(decimalFormat.format(cffGood.getWeight())));
    }

    private void executeLaffAlgorithm(List<Container> containers,
                                      List<BoxItem> products,
                                      Fleet fleet) {
        largestAreaFitFirstPackager.setShits(containers, true, true);
        List<Container> fits = largestAreaFitFirstPackager.packList(products, MAX_CONTAINERS, DEADLINE);
        printLaffAlgorithmExecutionResult(fits, fleet);
    }

    private void printLaffAlgorithmExecutionResult(List<Container> fits,
                                                   Fleet fleet) {
        System.out.println("Result for : " + fleet.getName() + " : \n" + fits);
    }
}
