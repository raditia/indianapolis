package com.gdn.mapper;

import com.gdn.MerchantResponseUtil;
import com.gdn.MerchantUtil;
import com.gdn.response.MerchantResponse;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MerchantResponseMapper.class)
public class MerchantResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(MerchantResponseMapper.class);
    }

    @Test
    public void toMerchantResponse() {
        given(MerchantResponseMapper.toMerchantResponse(MerchantUtil.merchantCompleteAttribute))
                .willReturn(MerchantResponseUtil.merchantResponseCompleteAttribute);
        MerchantResponse expectedResponse = MerchantResponseMapper.toMerchantResponse(MerchantUtil.merchantCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(MerchantResponseUtil.merchantResponseCompleteAttribute));
        verifyStatic(MerchantResponseMapper.class, times(1));
        MerchantResponseMapper.toMerchantResponse(MerchantUtil.merchantCompleteAttribute);
    }

    @Test
    public void toMerchantResponseList() {
        given(MerchantResponseMapper.toMerchantResponseList(MerchantUtil.merchantListCompleteAttribute))
                .willReturn(MerchantResponseUtil.merchantResponseListCompleteAttribute);
        List<MerchantResponse> expectedResponse = MerchantResponseMapper.toMerchantResponseList(MerchantUtil.merchantListCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        MerchantResponseMapper.toMerchantResponse(MerchantUtil.merchantCompleteAttribute);
        verifyStatic(MerchantResponseMapper.class, times(1));
        MerchantResponseMapper.toMerchantResponseList(MerchantUtil.merchantListCompleteAttribute);
    }
}