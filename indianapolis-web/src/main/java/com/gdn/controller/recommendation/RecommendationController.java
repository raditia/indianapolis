package com.gdn.controller.recommendation;

import com.gdn.recommendation.RecommendationService;
import com.gdn.response.FleetRecommendationResponse;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.SchedulingResponse;
import com.gdn.response.WebResponse;
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
            value = "/execute",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void recommendation(){
        recommendationService.executeBatch();
    }

    @RequestMapping(
            value = "/result",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<RecommendationResponse> getAllRecommendationFleetResult(){
        return recommendationService.findAllRecommendationFleetResult();
    }

    @RequestMapping(
            value = "/pickup",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void chooseRecommendationAndInsertToDatabase(@RequestParam("id") String recommendationResultId,
                                                        @RequestParam("pickupDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pickupDate){
        recommendationService.choosePickupAndSendEmail(recommendationResultId, pickupDate);
    }

}
