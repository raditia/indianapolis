package com.gdn.mapper;

import com.gdn.CffRequestUtil;
import com.gdn.CffUtil;
import com.gdn.entity.Cff;
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
@PrepareForTest(CffMapper.class)
public class CffMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(CffMapper.class);
    }

    @Test
    public void toCff() {
        given(CffMapper.toCff(CffRequestUtil.cffRequestCompleteAttributeExistingMerchantExistingPickupPoint))
                .willReturn(CffUtil.cffCompleteAttribute);
        Cff expectedResponse = CffMapper.toCff(CffRequestUtil.cffRequestCompleteAttributeExistingMerchantExistingPickupPoint);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(CffUtil.cffCompleteAttribute));
        verifyStatic(CffMapper.class, times(1));
        CffMapper.toCff(CffRequestUtil.cffRequestCompleteAttributeExistingMerchantExistingPickupPoint);
    }
}