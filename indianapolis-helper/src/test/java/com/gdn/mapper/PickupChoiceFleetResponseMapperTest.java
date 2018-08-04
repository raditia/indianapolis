package com.gdn.mapper;

import com.gdn.PickupChoiceFleetResponseUtil;
import com.gdn.PickupFleetUtil;
import com.gdn.response.PickupChoiceFleetResponse;
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
@PrepareForTest(PickupChoiceFleetResponseMapper.class)
public class PickupChoiceFleetResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(PickupChoiceFleetResponseMapper.class);
    }

    @Test
    public void toPickupChoiceFleetResponse() {
        given(PickupChoiceFleetResponseMapper.toPickupChoiceFleetResponse(PickupFleetUtil.pickupFleetMinusPickup))
                .willReturn(PickupChoiceFleetResponseUtil.pickupChoiceFleetResponse);
        PickupChoiceFleetResponse expectedResponse = PickupChoiceFleetResponseMapper.toPickupChoiceFleetResponse(PickupFleetUtil.pickupFleetMinusPickup);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupChoiceFleetResponseUtil.pickupChoiceFleetResponse));
        verifyStatic(PickupChoiceFleetResponseMapper.class, times(1));
        PickupChoiceFleetResponseMapper.toPickupChoiceFleetResponse(PickupFleetUtil.pickupFleetMinusPickup);
    }

    @Test
    public void toPickupChoiceFleetResponseList() {
        given(PickupChoiceFleetResponseMapper.toPickupChoiceFleetResponseList(PickupFleetUtil.pickupFleetListMinusPickup))
                .willReturn(PickupChoiceFleetResponseUtil.pickupChoiceFleetResponseList);
        List<PickupChoiceFleetResponse> expectedResponse = PickupChoiceFleetResponseMapper.toPickupChoiceFleetResponseList(PickupFleetUtil.pickupFleetListMinusPickup);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(PickupChoiceFleetResponseUtil.pickupChoiceFleetResponseList));
        verifyStatic(PickupChoiceFleetResponseMapper.class, times(1));
        PickupChoiceFleetResponseMapper.toPickupChoiceFleetResponseList(PickupFleetUtil.pickupFleetListMinusPickup);
    }
}