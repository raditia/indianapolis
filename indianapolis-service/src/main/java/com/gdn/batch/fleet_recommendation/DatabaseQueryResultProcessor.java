package com.gdn.batch.fleet_recommendation;

import com.gdn.cff.CffService;
import com.gdn.recommendation.*;
import com.gdn.recommendation_algorithm.RecommendationProcessorService;
import com.gdn.helper.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class DatabaseQueryResultProcessor implements ItemProcessor<DatabaseQueryResult, List<Recommendation>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueryResultProcessor.class);
    private List<DatabaseQueryResult> resultList = new ArrayList<>();

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private CffService cffService;
    @Autowired
    private RecommendationProcessorService recommendationProcessorService;

    private int rowCount;
    private String warehouseId;

    public DatabaseQueryResultProcessor(String warehouseId, String rowCount) {
        this.warehouseId=warehouseId;
        this.rowCount=Integer.parseInt(rowCount);
        LOGGER.info("Row count : " + rowCount);
    }

    @Override
    public List<Recommendation> process(DatabaseQueryResult databaseQueryResult) throws Exception {
        resultList.add(databaseQueryResult);
        List<Recommendation> recommendationList = new ArrayList<>();
        if(allItemsHaveBeenRetrieved()){
            LOGGER.info("Processing...");
            recommendationList = recommendationProcessorService.getThreeRecommendation(resultList, warehouseId);
            for (DatabaseQueryResult item : resultList) {
                cffService.updateSchedulingStatus(item.getCffId());
            }
            resultList.clear();
        }
        return recommendationList;
    }

    private boolean allItemsHaveBeenRetrieved(){
        return resultList.size() == rowCount;
    }


}
