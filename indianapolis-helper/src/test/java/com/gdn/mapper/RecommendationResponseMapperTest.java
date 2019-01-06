package com.gdn.mapper;

import com.gdn.RecommendationResponseUtil;
import com.gdn.RecommendationResultUtil;
import com.gdn.response.RecommendationResponse;
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
@PrepareForTest(RecommendationResponseMapper.class)
public class RecommendationResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(RecommendationResponseMapper.class);
    }

    @Test
    public void toRecommendationResponse() {
        given(RecommendationResponseMapper.toRecommendationResponse(RecommendationResultUtil.recommendationResultListCompleteAttribute))
                .willReturn(RecommendationResponseUtil.recommendationResponseCompleteAttribute);
        RecommendationResponse expectedResponse = RecommendationResponseMapper.toRecommendationResponse(RecommendationResultUtil.recommendationResultListCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(RecommendationResponseUtil.recommendationResponseCompleteAttribute));
        verifyStatic(RecommendationResponseMapper.class, times(1));
        RecommendationResponseMapper.toRecommendationResponse(RecommendationResultUtil.recommendationResultListCompleteAttribute);
    }
}