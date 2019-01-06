package com.gdn.mapper;

import com.gdn.WarehouseCategoryResponseUtil;
import com.gdn.WarehouseCategoryUtil;
import com.gdn.response.WarehouseCategoryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WarehouseCategoryResponseMapper.class)
public class WarehouseCategoryResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(WarehouseCategoryResponseMapper.class);
    }

    @Test
    public void toWarehouseCategoryResponse() {
        given(WarehouseCategoryResponseMapper.toWarehouseCategoryResponse(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute))
                .willReturn(WarehouseCategoryResponseUtil.warehouseCategoryResponseCompleteAttribute);
        WarehouseCategoryResponse expectedResponse = WarehouseCategoryResponseMapper.toWarehouseCategoryResponse(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WarehouseCategoryResponseUtil.warehouseCategoryResponseCompleteAttribute));
        verifyStatic(WarehouseCategoryResponseMapper.class, times(1));
        WarehouseCategoryResponseMapper.toWarehouseCategoryResponse(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute);
    }

    @Test
    public void toWarehouseCategoryResponseList() {
        given(WarehouseCategoryResponseMapper.toWarehouseCategoryResponseList(WarehouseCategoryUtil.warehouseCategoryListCompleteAttribute))
                .willReturn(WarehouseCategoryResponseUtil.warehouseCategoryResponseListCompleteAttribute);
        List<WarehouseCategoryResponse> expectedResponse = WarehouseCategoryResponseMapper.toWarehouseCategoryResponseList(WarehouseCategoryUtil.warehouseCategoryListCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WarehouseCategoryResponseUtil.warehouseCategoryResponseListCompleteAttribute));
        verifyStatic(WarehouseCategoryResponseMapper.class, times(1));
        WarehouseCategoryResponseMapper.toWarehouseCategoryResponseList(WarehouseCategoryUtil.warehouseCategoryListCompleteAttribute);
    }
}