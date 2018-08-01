package com.gdn.pickup.implementation;

import com.gdn.entity.Pickup;
import com.gdn.entity.PickupDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.mapper.PickupDetailMapper;
import com.gdn.repository.PickupRepository;
import com.gdn.repository.RecommendationResultRepository;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.util.PickupChoiceRequestUtil;
import com.gdn.util.RecommendationFleetUtil;
import com.gdn.util.RecommendationResultUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PickupServiceImplTest {

    @Mock
    private RecommendationResultRepository recommendationResultRepository;
    @Mock
    private PickupRepository pickupRepository;

    @InjectMocks
    private PickupServiceImpl pickupService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void savePickup() {
        List<Pickup> pickupList = new ArrayList<>();
        given(recommendationResultRepository
                .getOne(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute.getRecommendationResultId()))
                .willReturn(RecommendationResultUtil.recommendationResultCompleteAttribute);

        List<RecommendationFleet> recommendationFleetList = RecommendationResultUtil.recommendationResultCompleteAttribute.getRecommendationFleetList();
        for (RecommendationFleet recommendationFleet:recommendationFleetList
                ) {
            String pickupId = "pickup_" + UUID.randomUUID().toString();
            String plateNumber = "plate_number_" + UUID.randomUUID().toString();
            List<PickupDetail> pickupDetailList = PickupDetailMapper.toPickupDetailList(recommendationFleet.getRecommendationDetailList());
            Pickup pickup = Pickup.builder()
                    .id(pickupId)
                    .pickupDate(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute.getPickupDate())
                    .fleet(recommendationFleet.getFleet())
                    .plateNumber(plateNumber)
                    .warehouse(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse())
                    .pickupDetailList(pickupDetailList)
                    .build();
            pickupList.add(pickup);
            given(pickupRepository.save(pickup)).willReturn(pickup);
        }
        List<Pickup> expectedResponse = pickupService.savePickup(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(pickupList));
        verify(recommendationResultRepository, times(1)).getOne(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute.getRecommendationResultId());
    }

    @After
    public void tearDown() throws Exception {
    }
}