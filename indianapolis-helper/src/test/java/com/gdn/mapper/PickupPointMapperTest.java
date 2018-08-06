package com.gdn.mapper;

import com.gdn.PickupPointRequestUtil;
import com.gdn.PickupPointUtil;
import com.gdn.entity.PickupPoint;
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
@PrepareForTest(PickupPointMapper.class)
public class PickupPointMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(PickupPointMapper.class);
    }

    @Test
    public void toPickupPoint() {
        given(PickupPointMapper.toPickupPoint(PickupPointRequestUtil.existingPickupPointRequestCompleteAttribute))
                .willReturn(PickupPointUtil.pickupPointCompleteAttribute);
        PickupPoint expectedResponse = PickupPointMapper.toPickupPoint(PickupPointRequestUtil.existingPickupPointRequestCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupPointUtil.pickupPointCompleteAttribute));
        verifyStatic(PickupPointMapper.class, times(1));
        PickupPointMapper.toPickupPoint(PickupPointRequestUtil.existingPickupPointRequestCompleteAttribute);
    }

}