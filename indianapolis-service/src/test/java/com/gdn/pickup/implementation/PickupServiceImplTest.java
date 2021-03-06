package com.gdn.pickup.implementation;

import com.gdn.*;
import com.gdn.email.SendEmailService;
import com.gdn.entity.PickupDetail;
import com.gdn.entity.PickupFleet;
import com.gdn.entity.Pickup;
import com.gdn.mapper.PickupMapper;
import com.gdn.repository.PickupRepository;
import com.gdn.repository.RecommendationResultRepository;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.MessagingException;

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
    @Mock
    private SendEmailService sendEmailService;

    @InjectMocks
    private PickupServiceImpl pickupService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(PickupMapper.class);
    }

    @Test
    public void savePickup() throws MessagingException {
        given(recommendationResultRepository
                .getOne(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute.getRecommendationResultId()))
                .willReturn(RecommendationResultUtil.recommendationResultCompleteAttribute);
        given(PickupMapper.toPickup(RecommendationResultUtil.recommendationResultCompleteAttribute, FleetChoiceRequestUtil.fleetChoiceRequestList)).willReturn(PickupUtil.pickupCompleteAttribute);
        Pickup pickup = PickupUtil.pickupCompleteAttribute;
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            pickupFleet.setPickup(pickup);
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                pickupDetail.setPickupFleet(pickupFleet);
            }
        }
        given(pickupRepository.save(pickup)).willReturn(pickup);
        sendEmailService.sendEmail(pickup);
        WebResponse<PickupChoiceResponse> expectedResponse = pickupService.savePickup(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute);
        recommendationResultRepository.deleteAllByWarehouse(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(PickupChoiceResponseUtil.pickupChoiceResponseCompleteAttribute)));

        InOrder inOrder = Mockito.inOrder(recommendationResultRepository, pickupRepository, sendEmailService);
        inOrder.verify(recommendationResultRepository, times(1)).getOne(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute.getRecommendationResultId());
        inOrder.verify(recommendationResultRepository, times(1)).deleteAllByWarehouse(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse());
        inOrder.verify(pickupRepository, times(1)).save(pickup);
        inOrder.verify(sendEmailService, times(1)).sendEmail(pickup);
    }

    @After
    public void tearDown() throws Exception {
    }
}