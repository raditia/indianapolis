package com.gdn.recommendation_algorithm.implementation;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Product;
import com.gdn.recommendation.Recommendation;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import com.gdn.recommendation_algorithm.Helper;
import com.gdn.recommendation_algorithm.RecommendationProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationProcessorImpl implements RecommendationProcessorService {

    @Autowired
    private FleetProcessorService fleetProcessorService;

    @Override
    public List<Recommendation> getThreeRecommendation(List<DatabaseQueryResult> productResultList, String warehouseId) {
        List<Recommendation> threeRecommendations = new ArrayList<>();
        List<Product> productList;

        List<Fleet> topThreeFleets = fleetProcessorService.getTopThreeFleetsWillUsed(productResultList);
        productList = Helper.migrateIntoProductList(productResultList);
        Recommendation recommendation = buildRecommendation(productList, topThreeFleets, warehouseId);
        threeRecommendations.add(recommendation);

        for (Recommendation threeRecommendation : threeRecommendations) {
            System.out.println(threeRecommendation);
        }
        return threeRecommendations;
    }

    private Recommendation buildRecommendation(List<Product> productList, List<Fleet> topThreeFleets, String warehouseId) {
        Float cbmTotal = 0.0f;
        int productAmount = 0;

        for (Product product : productList) {
            System.out.println(product);
            cbmTotal = Helper.formatNormalFloat(cbmTotal + product.getCbm());
            productAmount += product.getQuantity();
        }

        return Recommendation.builder()
                .id("recommendation_result_" + UUID.randomUUID().toString())
                .topThreeFleets(topThreeFleets)
                .cbmTotal(cbmTotal)
                .productList(productList)
                .productAmount(productAmount)
                .warehouseId(warehouseId)
                .build();
    }
}
