package com.gdn.controller.recommendation;

import com.gdn.pickup.PickupService;
import com.gdn.recommendation.RecommendationService;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.*;
import com.gdn.warehouse.WarehouseService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private PickupService pickupService;
    @Autowired
    private WarehouseService warehouseService;

    @RequestMapping(
            value = "/execute",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void recommendation() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
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
    public WebResponse<PickupChoiceResponse> chooseRecommendationAndInsertToDatabase(@RequestBody PickupChoiceRequest pickupChoiceRequest) throws MessagingException {
        return pickupService.savePickup(pickupChoiceRequest);
    }

    @RequestMapping(
            value = "/warehouse",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<WarehouseResponse>> findDistinctAllWarehouseInRecommendationResult(){
        return warehouseService.findDistinctAllWarehouseInRecommendationResult();
    }

}
