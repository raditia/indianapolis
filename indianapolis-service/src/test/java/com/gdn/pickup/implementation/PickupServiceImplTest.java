package com.gdn.pickup.implementation;

import com.gdn.entity.PickupDetail;
import com.gdn.entity.PickupFleet;
import com.gdn.mapper.PickupChoiceResponseMapper;
import com.gdn.entity.Pickup;
import com.gdn.mapper.PickupMapper;
import com.gdn.repository.PickupRepository;
import com.gdn.repository.RecommendationResultRepository;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.WebResponse;
import com.gdn.util.PickupChoiceRequestUtil;
import com.gdn.util.PickupUtil;
import com.gdn.util.RecommendationResultUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PickupMapper.class})
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
        PowerMockito.mockStatic(PickupMapper.class);
    }

    @Test
    public void savePickup() {
        given(recommendationResultRepository
                .getOne(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute.getRecommendationResultId()))
                .willReturn(RecommendationResultUtil.recommendationResultCompleteAttribute);
        given(PickupMapper.toPickup(RecommendationResultUtil.recommendationResultCompleteAttribute)).willReturn(PickupUtil.pickupCompleteAttribute);
        Pickup pickup = PickupUtil.pickupCompleteAttribute;
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            pickupFleet.setPickup(pickup);
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                pickupDetail.setPickupFleet(pickupFleet);
            }
        }
        given(pickupRepository.save(pickup)).willReturn(pickup);
        WebResponse<PickupChoiceResponse> expectedResponse = pickupService.savePickup(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute);
        recommendationResultRepository.deleteAllByWarehouse(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(PickupChoiceResponseMapper.toPickupChoiceResponse(pickup))));

        InOrder inOrder = Mockito.inOrder(recommendationResultRepository, pickupRepository);
        inOrder.verify(recommendationResultRepository, times(1)).getOne(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute.getRecommendationResultId());
        inOrder.verify(pickupRepository, times(1)).save(pickup);
        inOrder.verify(recommendationResultRepository, times(1)).deleteAllByWarehouse(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse());
    }

    @After
    public void tearDown() throws Exception {
    }
}