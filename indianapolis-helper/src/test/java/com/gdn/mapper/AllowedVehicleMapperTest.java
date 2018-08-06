package com.gdn.mapper;

import com.gdn.AllowedVehicleRequestUtil;
import com.gdn.AllowedVehicleUtil;
import com.gdn.entity.AllowedVehicle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AllowedVehicleMapper.class)
public class AllowedVehicleMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(AllowedVehicleMapper.class);
    }

    @Test
    public void toAllowedVehicle() {
        given(AllowedVehicleMapper.toAllowedVehicle(AllowedVehicleRequestUtil.allowedVehicleRequestCompleteAttribute))
                .willReturn(AllowedVehicleUtil.allowedVehicleCompleteAttribute);
        AllowedVehicle expectedResponse = AllowedVehicleMapper.toAllowedVehicle(AllowedVehicleRequestUtil.allowedVehicleRequestCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(AllowedVehicleUtil.allowedVehicleCompleteAttribute));
        verifyStatic(AllowedVehicleMapper.class, times(1));
        AllowedVehicleMapper.toAllowedVehicle(AllowedVehicleRequestUtil.allowedVehicleRequestCompleteAttribute);
    }

    @Test
    public void toAllowedVehicleList() {
        given(AllowedVehicleMapper.toAllowedVehicleList(AllowedVehicleRequestUtil.allowedVehicleRequestListCompleteAttribute))
                .willReturn(AllowedVehicleUtil.allowedVehicleListCompleteAttribute);
        List<AllowedVehicle> expectedResponse = AllowedVehicleMapper.toAllowedVehicleList(AllowedVehicleRequestUtil.allowedVehicleRequestListCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(AllowedVehicleUtil.allowedVehicleListCompleteAttribute));
        verifyStatic(AllowedVehicleMapper.class, times(1));
        AllowedVehicleMapper.toAllowedVehicleList(AllowedVehicleRequestUtil.allowedVehicleRequestListCompleteAttribute);
    }
}