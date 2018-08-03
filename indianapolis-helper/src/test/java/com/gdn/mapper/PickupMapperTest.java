package com.gdn.mapper;

import com.gdn.PickupUtil;
import com.gdn.RecommendationResultUtil;
import com.gdn.entity.Pickup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PickupMapper.class)
public class PickupMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(PickupMapper.class);
    }

    @Test
    public void toPickup() {
        given(PickupMapper.toPickup(RecommendationResultUtil.recommendationResultCompleteAttribute))
                .willReturn(PickupUtil.pickupCompleteAttribute);
        Pickup expectedResponse = PickupMapper.toPickup(RecommendationResultUtil.recommendationResultCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupUtil.pickupCompleteAttribute));
        verifyStatic(PickupMapper.class, times(1));
        PickupMapper.toPickup(RecommendationResultUtil.recommendationResultCompleteAttribute);
    }
}