package com.gdn.mapper;

import com.gdn.CffGoodUtil;
import com.gdn.CffResponseUtil;
import com.gdn.CffUtil;
import com.gdn.response.CffResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CffResponseMapper.class)
public class CffResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        spy(CffResponseMapper.class);
    }

    @Test
    public void toCffResponse() throws Exception {
        PowerMockito
                .doReturn(CffGoodUtil.cffGoodsCbmTotal)
                .when(CffResponseMapper.class, "getCbmTotal", CffGoodUtil.cffGoodListMinusCff);
        given(CffResponseMapper
                .toCffResponse(CffUtil.cffCompleteAttribute))
                .willReturn(CffResponseUtil.cffResponseCompleteAttribute);
        CffResponse expectedResponse = CffResponseMapper.toCffResponse(CffUtil.cffCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(CffResponseUtil.cffResponseCompleteAttribute));
    }

    @Test
    public void toCffResponseList() {
        given(CffResponseMapper.toCffListResponse(CffUtil.cffListCompleteAttribute))
                .willReturn(CffResponseUtil.cffResponseListCompleteAttribute);
        List<CffResponse> expectedResponse = CffResponseMapper.toCffListResponse(CffUtil.cffListCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(CffResponseUtil.cffResponseListCompleteAttribute));
    }
}