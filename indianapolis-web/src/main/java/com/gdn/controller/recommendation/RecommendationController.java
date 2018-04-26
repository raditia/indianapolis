package com.gdn.controller.recommendation;

import com.gdn.entity.RecommendationFleet;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public boolean recommendation(){
        return recommendationService.executeBatch();
    }

    @RequestMapping(
            value = "/result",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<RecommendationFleet> getAllRecommendationFleetResult(){
        return recommendationService.findAllRecommendationFleetResult();
    }

}
