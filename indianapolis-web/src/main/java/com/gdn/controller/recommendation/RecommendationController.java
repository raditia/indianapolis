package com.gdn.controller.recommendation;

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
@RequestMapping(value = "/api")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @RequestMapping(
            value = "/recommendation",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DatabaseQueryResult> recommendation(){
        return recommendationService.executeBatch();
    }

}
