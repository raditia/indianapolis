package com.gdn.mapper;

import com.gdn.PickupFleetUtil;
import com.gdn.RecommendationFleetUtil;
import com.gdn.entity.PickupFleet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PickupFleetMapper.class)
public class PickupFleetMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(PickupFleetMapper.class);
    }

    @Test
    public void toPickupFleet() {
        given(PickupFleetMapper.toPickupFleet(RecommendationFleetUtil.recommendationFleetMinusRecommendationResult))
                .willReturn(PickupFleetUtil.pickupFleetMinusPickup);
        PickupFleet expectedResponse = PickupFleetMapper.toPickupFleet(RecommendationFleetUtil.recommendationFleetMinusRecommendationResult);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupFleetUtil.pickupFleetMinusPickup));
        verifyStatic(PickupFleetMapper.class, times(1));
        PickupFleetMapper.toPickupFleet(RecommendationFleetUtil.recommendationFleetMinusRecommendationResult);
    }

    @Test
    public void toPickupFleetList() {
        given(PickupFleetMapper.toPickupFleetList(RecommendationFleetUtil.recommendationFleetListMinusRecommendationResult))
                .willReturn(PickupFleetUtil.pickupFleetListMinusPickup);
        List<PickupFleet> expectedResponse = PickupFleetMapper.toPickupFleetList(RecommendationFleetUtil.recommendationFleetListMinusRecommendationResult);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(PickupFleetUtil.pickupFleetListMinusPickup));
        verifyStatic(PickupFleetMapper.class, times(1));
        PickupFleetMapper.toPickupFleetList(RecommendationFleetUtil.recommendationFleetListMinusRecommendationResult);
    }

}