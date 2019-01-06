package com.gdn.mapper;

import com.gdn.PickupDetailUtil;
import com.gdn.RecommendationDetailUtil;
import com.gdn.entity.PickupDetail;
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
@PrepareForTest(PickupDetailMapper.class)
public class PickupDetailMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(PickupDetailMapper.class);
    }

    @Test
    public void toPickupDetail() {
        given(PickupDetailMapper.toPickupDetail(RecommendationDetailUtil.recommendationDetailMinusRecommendationFleet))
                .willReturn(PickupDetailUtil.pickupDetailMinusPickupFleet);
        PickupDetail expectedResponse = PickupDetailMapper.toPickupDetail(RecommendationDetailUtil.recommendationDetailMinusRecommendationFleet);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupDetailUtil.pickupDetailMinusPickupFleet));
        verifyStatic(PickupDetailMapper.class, times(1));
        PickupDetailMapper.toPickupDetail(RecommendationDetailUtil.recommendationDetailMinusRecommendationFleet);
    }

    @Test
    public void toPickupDetailList() {
        given(PickupDetailMapper.toPickupDetailList(RecommendationDetailUtil.recommendationDetailListMinusRecommendationFleet))
                .willReturn(PickupDetailUtil.pickupDetailListMinusPickupFleet);
        List<PickupDetail> expectedResponse = PickupDetailMapper.toPickupDetailList(RecommendationDetailUtil.recommendationDetailListMinusRecommendationFleet);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(PickupDetailUtil.pickupDetailListMinusPickupFleet));
        verifyStatic(PickupDetailMapper.class, times(1));
        PickupDetailMapper.toPickupDetailList(RecommendationDetailUtil.recommendationDetailListMinusRecommendationFleet);
    }
}