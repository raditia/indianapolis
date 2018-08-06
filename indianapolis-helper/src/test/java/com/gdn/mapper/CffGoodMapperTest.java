package com.gdn.mapper;

import com.gdn.CffGoodRequestUtil;
import com.gdn.CffGoodUtil;
import com.gdn.entity.CffGood;
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
@PrepareForTest(CffGoodMapper.class)
public class CffGoodMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(CffGoodMapper.class);
    }

    @Test
    public void toCffGood() {
        given(CffGoodMapper.toCffGood(CffGoodRequestUtil.cffGood1RequestCompleteAttribute))
                .willReturn(CffGoodUtil.cffGoodMinusCff1);
        CffGood expectedResponse = CffGoodMapper.toCffGood(CffGoodRequestUtil.cffGood1RequestCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(CffGoodUtil.cffGoodMinusCff1));
        verifyStatic(CffGoodMapper.class, times(1));
        CffGoodMapper.toCffGood(CffGoodRequestUtil.cffGood1RequestCompleteAttribute);
    }

    @Test
    public void toCffGoodList() {
        given(CffGoodMapper.toCffGoodList(CffGoodRequestUtil.cffGoodRequestListCompleteAttribute))
                .willReturn(CffGoodUtil.cffGoodListMinusCff);
        List<CffGood> expectedResponse = CffGoodMapper.toCffGoodList(CffGoodRequestUtil.cffGoodRequestListCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(CffGoodUtil.cffGoodListMinusCff));
        verifyStatic(CffGoodMapper.class, times(1));
        CffGoodMapper.toCffGoodList(CffGoodRequestUtil.cffGoodRequestListCompleteAttribute);
    }
}