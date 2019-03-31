package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.*;
import com.gdn.helper.DateHelper;
import com.gdn.laff.*;
import com.gdn.recommendation.DetailPickup;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Recommendation;
import com.gdn.repository.RecommendationResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FleetRecommendationWriter implements ItemWriter<List<Recommendation>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleetRecommendationWriter.class);

    private static final int maxContainers = 1000;  // maximum number of containers which can be used
    // limit search using 5 seconds deadline
    private static final double deadline = System.currentTimeMillis() + 50000;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Autowired
    private RecommendationResultRepository recommendationResultRepository;

    @Autowired
    private LargestAreaFitFirstPackager largestAreaFitFirstPackager;

    @Override
    public void write(List<? extends List<Recommendation>> items) throws Exception {
        List<Container> containers = new ArrayList<Container>();
        List<BoxItem> products = new ArrayList<BoxItem>();
        List<String> fleetList = new ArrayList<>();
        List<String> skuList = new ArrayList<>();

        for (List<Recommendation> recommendationList : items) {
            for (Recommendation recommendation : recommendationList) {
                List<RecommendationFleet> recommendationFleetList = populateRecommendationFleetList(recommendation);
                RecommendationResult recommendationResult = buildRecommendationResult(recommendation, recommendationFleetList);
                for (RecommendationFleet recommendationFleet : recommendationResult.getRecommendationFleetList()) {
                    if (!fleetList.contains(recommendationFleet.getFleet().getId())) {
                        fleetList.add(recommendationFleet.getFleet().getId());
                        // nambahin tipe kendaraan beserta ukurannya
                        Container container = new Container(
                                recommendationFleet.getFleet().getName(),
                                recommendationFleet.getFleet().getWidth(),
                                recommendationFleet.getFleet().getLength(),
                                recommendationFleet.getFleet().getHeight(),
                                recommendationFleet.getFleet().getWeight());
                        containers.add(container);
                        System.out.println(
                                container.getName() + " " +
                                        container.getWidth() + " " +
                                        container.getDepth() + " " +
                                        container.getHeight() + " " +
                                        container.getWeight()
                        );
                    }

                    recommendationFleet.setRecommendationResult(recommendationResult);

                    for (RecommendationDetail recommendationDetail : recommendationFleet.getRecommendationDetailList()) {
                        if (!skuList.contains(recommendationDetail.getCffGood().getSku())) {
                            skuList.add(recommendationDetail.getCffGood().getSku());
                            // nambahin item list yang perlu dibawa
                            BoxItem boxItem = new BoxItem(new Box(
                                    recommendationDetail.getCffGood().getSku(),
                                    Double.parseDouble(decimalFormat.format(recommendationDetail.getCffGood().getWidth())),
                                    Double.parseDouble(decimalFormat.format(recommendationDetail.getCffGood().getLength())),
                                    Double.parseDouble(decimalFormat.format(recommendationDetail.getCffGood().getHeight())),
                                    Double.parseDouble(decimalFormat.format(recommendationDetail.getCffGood().getWeight()))), recommendationDetail.getCffGood().getQuantity());
                            products.add(boxItem);
                            System.out.println(
                                    boxItem.getBox().getName() + " " +
                                            boxItem.getBox().getWidth() + " " +
                                            boxItem.getBox().getDepth() + " " +
                                            boxItem.getBox().getHeight() + " " +
                                            boxItem.getBox().getWeight() + " " + boxItem.getCount());
                        }

                        recommendationDetail.setRecommendationFleet(recommendationFleet);
                    }
                    recommendationResultRepository.save(recommendationResult);
                }
            }
        }
//        Packager packager = new LargestAreaFitFirstPackager(containers);
        largestAreaFitFirstPackager.setShits(containers, true, true);
        // match multiple containers
        List<Container> fits = largestAreaFitFirstPackager.packList(products, maxContainers, deadline);

        System.out.println("Hasil: \n" + fits);
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

}
