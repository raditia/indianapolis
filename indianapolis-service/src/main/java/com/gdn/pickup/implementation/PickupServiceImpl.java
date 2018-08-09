package com.gdn.pickup.implementation;

import com.gdn.entity.Pickup;
import com.gdn.entity.PickupDetail;
import com.gdn.entity.PickupFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.mapper.PickupChoiceResponseMapper;
import com.gdn.mapper.PickupMapper;
import com.gdn.pickup.PickupService;
import com.gdn.repository.PickupRepository;
import com.gdn.repository.RecommendationResultRepository;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PickupServiceImpl implements PickupService {

    @Autowired
    private RecommendationResultRepository recommendationResultRepository;
    @Autowired
    private PickupRepository pickupRepository;

    @Override
    @Transactional
    public WebResponse<PickupChoiceResponse> savePickup(PickupChoiceRequest pickupChoiceRequest) {
        RecommendationResult chosenRecommendation = recommendationResultRepository.getOne(pickupChoiceRequest.getRecommendationResultId());
        Pickup pickup = PickupMapper.toPickup(chosenRecommendation, pickupChoiceRequest.getFleetChoiceRequestList());
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            pickupFleet.setPickup(pickup);
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                pickupDetail.setPickupFleet(pickupFleet);
            }
        }
        recommendationResultRepository.deleteAllByWarehouse(chosenRecommendation.getWarehouse());
        return WebResponse.OK(PickupChoiceResponseMapper.toPickupChoiceResponse(pickupRepository.save(pickup)));
    }

}
