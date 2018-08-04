package com.gdn.mapper;

import com.gdn.PickupChoiceResponseUtil;
import com.gdn.PickupUtil;
import com.gdn.response.PickupChoiceResponse;
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
@PrepareForTest(PickupChoiceResponseMapper.class)
public class PickupChoiceResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(PickupChoiceResponseMapper.class);
    }

    @Test
    public void toPickupChoiceResponse() {
        given(PickupChoiceResponseMapper.toPickupChoiceResponse(PickupUtil.pickupCompleteAttribute))
                .willReturn(PickupChoiceResponseUtil.pickupChoiceResponseCompleteAttribute);
        PickupChoiceResponse expectedResponse = PickupChoiceResponseMapper.toPickupChoiceResponse(PickupUtil.pickupCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupChoiceResponseUtil.pickupChoiceResponseCompleteAttribute));
        verifyStatic(PickupChoiceResponseMapper.class, times(1));
        PickupChoiceResponseMapper.toPickupChoiceResponse(PickupUtil.pickupCompleteAttribute);
    }
}