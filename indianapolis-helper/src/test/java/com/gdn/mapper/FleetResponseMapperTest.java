package com.gdn.mapper;

import com.gdn.FleetResponseUtil;
import com.gdn.FleetUtil;
import com.gdn.response.FleetResponse;
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
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FleetResponseMapper.class)
public class FleetResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(FleetResponseMapper.class);
    }

    @Test
    public void toFleetResponse() {
        given(FleetResponseMapper.toFleetResponse(FleetUtil.fleetMotorCompleteAttribute))
                .willReturn(FleetResponseUtil.fleetResponseCompleteAttribute);
        FleetResponse expectedResponse = FleetResponseMapper.toFleetResponse(FleetUtil.fleetMotorCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(FleetResponseMapper.toFleetResponse(FleetUtil.fleetMotorCompleteAttribute)));
    }

    @Test
    public void toFleetResponseList() {
        given(FleetResponseMapper.toFleetResponseList(FleetUtil.fleetListMotorOnly))
                .willReturn(FleetResponseUtil.fleetResponseListCompleteAttribute);
        List<FleetResponse> expectedResponse = FleetResponseMapper.toFleetResponseList(FleetUtil.fleetListMotorOnly);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(FleetResponseUtil.fleetResponseListCompleteAttribute));
    }
}