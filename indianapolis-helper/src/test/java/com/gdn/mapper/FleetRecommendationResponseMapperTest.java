package com.gdn.mapper;

import com.gdn.FleetRecommendationResponseUtil;
import com.gdn.RecommendationResultUtil;
import com.gdn.response.FleetRecommendationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FleetRecommendationResponseMapper.class)
public class FleetRecommendationResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(FleetRecommendationResponseMapper.class);
    }

    @Test
    public void toRecommendationResponse() {
        given(FleetRecommendationResponseMapper.toFleetRecommendationResponse(RecommendationResultUtil.recommendationResultCompleteAttribute))
                .willReturn(FleetRecommendationResponseUtil.fleetRecommendationResponseUtilCompleteAttribute);
        FleetRecommendationResponse expectedResponse = FleetRecommendationResponseMapper.toFleetRecommendationResponse(RecommendationResultUtil.recommendationResultCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(FleetRecommendationResponseUtil.fleetRecommendationResponseUtilCompleteAttribute));
        verifyStatic(FleetRecommendationResponseMapper.class, times(1));
        FleetRecommendationResponseMapper.toFleetRecommendationResponse(RecommendationResultUtil.recommendationResultCompleteAttribute);
    }
}