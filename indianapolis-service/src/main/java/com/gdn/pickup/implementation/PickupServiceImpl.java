package com.gdn.pickup.implementation;

import com.gdn.entity.Pickup;
import com.gdn.entity.PickupDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.pickup.PickupService;
import com.gdn.repository.PickupRepository;
import com.gdn.repository.RecommendationResultRepository;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.mapper.PickupDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PickupServiceImpl implements PickupService {

    @Autowired
    private RecommendationResultRepository recommendationResultRepository;
    @Autowired
    private PickupRepository pickupRepository;

    @Override
    public List<Pickup> savePickup(PickupChoiceRequest pickupChoiceRequest) {
        List<Pickup> pickupList = new ArrayList<>();
        List<PickupDetail> pickupDetailList;
        RecommendationResult recommendationResult = recommendationResultRepository.getOne(pickupChoiceRequest.getRecommendationResultId());
        List<RecommendationFleet> recommendationFleetList = recommendationResult.getRecommendationFleetList();
        for (RecommendationFleet recommendationFleet:recommendationFleetList
                ) {
            pickupDetailList = PickupDetailMapper.toPickupDetailList(recommendationFleet.getRecommendationDetailList());
            Pickup pickup = Pickup.builder()
                    .id("pickup_" + UUID.randomUUID().toString())
                    .pickupDate(pickupChoiceRequest.getPickupDate())
                    .fleet(recommendationFleet.getFleet())
                    .plateNumber("plate_number_" + UUID.randomUUID().toString())
                    .warehouse(recommendationResult.getWarehouse())
                    .pickupDetailList(pickupDetailList)
                    .build();
            pickupList.add(pickup);
            pickupRepository.save(pickup);
        }
        return pickupList;
    }

}
