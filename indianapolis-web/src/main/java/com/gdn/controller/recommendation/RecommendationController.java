package com.gdn.controller.recommendation;

import com.gdn.pickup.PickupService;
import com.gdn.recommendation.RecommendationService;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.*;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private PickupService pickupService;

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
    public WebResponse<RecommendationResponse> getAllRecommendationFleetResult(@RequestParam("warehouseId") String warehouseId){
        return recommendationService.findAllRecommendationFleetResult(warehouseId);
    }

    @RequestMapping(
            value = "/pickup",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<PickupChoiceResponse> chooseRecommendationAndInsertToDatabase(@RequestBody PickupChoiceRequest pickupChoiceRequest) throws MessagingException, IOException, DocumentException {
        return pickupService.savePickup(pickupChoiceRequest);
    }

}
