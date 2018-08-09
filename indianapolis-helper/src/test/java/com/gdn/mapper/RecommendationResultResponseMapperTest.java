package com.gdn.mapper;

import com.gdn.RecommendationResultResponseUtil;
import com.gdn.RecommendationResultUtil;
import com.gdn.response.RecommendationResultResponse;
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
@PrepareForTest(RecommendationResultResponseMapper.class)
public class RecommendationResultResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(RecommendationResultResponseMapper.class);
    }

    @Test
    public void toRecommendationResponse() {
        given(RecommendationResultResponseMapper.toRecommendationResultResponse(RecommendationResultUtil.recommendationResultCompleteAttribute))
                .willReturn(RecommendationResultResponseUtil.recommendationResultResponseUtilCompleteAttribute);
        RecommendationResultResponse expectedResponse = RecommendationResultResponseMapper.toRecommendationResultResponse(RecommendationResultUtil.recommendationResultCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(RecommendationResultResponseUtil.recommendationResultResponseUtilCompleteAttribute));
        verifyStatic(RecommendationResultResponseMapper.class, times(1));
        RecommendationResultResponseMapper.toRecommendationResultResponse(RecommendationResultUtil.recommendationResultCompleteAttribute);
    }
}