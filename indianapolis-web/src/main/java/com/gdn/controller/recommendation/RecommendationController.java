package com.gdn.controller.recommendation;

import com.gdn.entity.RecommendationDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public boolean recommendation(@RequestParam("warehouseId") String warehouseId){
        return recommendationService.executeBatch(warehouseId);
    }

    @RequestMapping(
            value = "/result",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<RecommendationFleet> getAllRecommendationFleetResult(){
        return recommendationService.findAllRecommendationFleetResult();
    }

    @RequestMapping(
            value = "/pickup",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void chooseRecommendationAndInsertToDatabase(@RequestParam("recommendationFleetId") String recommendationFleetId,
                                                        @RequestParam("pickupDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pickupDate){
        recommendationService.choosePickupAndSendEmail(recommendationFleetId, pickupDate);
    }

}
