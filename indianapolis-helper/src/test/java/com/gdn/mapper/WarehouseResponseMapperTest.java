package com.gdn.mapper;

import com.gdn.WarehouseResponseUtil;
import com.gdn.WarehouseUtil;
import com.gdn.response.WarehouseResponse;
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
@PrepareForTest(WarehouseResponseMapper.class)
public class WarehouseResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(WarehouseResponseMapper.class);
    }

    @Test
    public void toWarehouseResponse() {
        given(WarehouseResponseMapper.toWarehouseResponse(WarehouseUtil.warehouseMinusWarehouseCategoryList))
                .willReturn(WarehouseResponseUtil.warehouseResponseCompleteAttribute);
        WarehouseResponse expectedResponse = WarehouseResponseMapper.toWarehouseResponse(WarehouseUtil.warehouseMinusWarehouseCategoryList);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WarehouseResponseUtil.warehouseResponseCompleteAttribute));
        verifyStatic(WarehouseResponseMapper.class, times(1));
        WarehouseResponseMapper.toWarehouseResponse(WarehouseUtil.warehouseMinusWarehouseCategoryList);
    }

    @Test
    public void toWarehouseResponseList() {
        given(WarehouseResponseMapper.toWarehouseResponseList(WarehouseUtil.warehouseListMinusWarehouseCategoryList))
                .willReturn(WarehouseResponseUtil.warehouseResponseListCompleteAttribute);
        List<WarehouseResponse> expectedResponse = WarehouseResponseMapper.toWarehouseResponseList(WarehouseUtil.warehouseListMinusWarehouseCategoryList);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WarehouseResponseUtil.warehouseResponseListCompleteAttribute));
        verifyStatic(WarehouseResponseMapper.class, times(1));
        WarehouseResponseMapper.toWarehouseResponseList(WarehouseUtil.warehouseListMinusWarehouseCategoryList);
    }
}