package com.gdn.mapper;

import com.gdn.LogisticVendorResponseUtil;
import com.gdn.LogisticVendorUtil;
import com.gdn.response.LogisticVendorResponse;
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
@PrepareForTest(LogisticVendorResponseMapper.class)
public class LogisticVendorResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(LogisticVendorResponseMapper.class);
    }

    @Test
    public void toLogisticVendorResponse() {
        given(LogisticVendorResponseMapper.toLogisticVendorResponse(LogisticVendorUtil.logisticVendorCompleteAttribute))
                .willReturn(LogisticVendorResponseUtil.logisticVendorResponse);
        LogisticVendorResponse expectedResponse = LogisticVendorResponseMapper.toLogisticVendorResponse(LogisticVendorUtil.logisticVendorCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(LogisticVendorResponseUtil.logisticVendorResponse));
        verifyStatic(LogisticVendorResponseMapper.class, times(1));
        LogisticVendorResponseMapper.toLogisticVendorResponse(LogisticVendorUtil.logisticVendorCompleteAttribute);
    }

    @Test
    public void toLogisticVendorResponseList() {
        given(LogisticVendorResponseMapper.toLogisticVendorResponseList(LogisticVendorUtil.logisticVendorListCompleteAttribute))
                .willReturn(LogisticVendorResponseUtil.logisticVendorResponseList);
        List<LogisticVendorResponse> expectedResponse = LogisticVendorResponseMapper.toLogisticVendorResponseList(LogisticVendorUtil.logisticVendorListCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(LogisticVendorResponseUtil.logisticVendorResponseList));
        verifyStatic(LogisticVendorResponseMapper.class, times(1));
        LogisticVendorResponseMapper.toLogisticVendorResponseList(LogisticVendorUtil.logisticVendorListCompleteAttribute);
    }
}