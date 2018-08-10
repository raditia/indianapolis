package com.gdn.mapper;

import com.gdn.LogisticVendorFleetUtil;
import com.gdn.LogisticVendorUtil;
import com.gdn.entity.LogisticVendor;
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
@PrepareForTest(LogisticVendorMapper.class)
public class LogisticVendorMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(LogisticVendorMapper.class);
    }

    @Test
    public void toLogisticVendor() {
        given(LogisticVendorMapper.toLogisticVendor(LogisticVendorFleetUtil.logisticVendorFleetLogisticVendorOnly))
                .willReturn(LogisticVendorUtil.logisticVendorCompleteAttribute);
        LogisticVendor expectedResponse = LogisticVendorMapper.toLogisticVendor(LogisticVendorFleetUtil.logisticVendorFleetLogisticVendorOnly);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(LogisticVendorUtil.logisticVendorCompleteAttribute));
        verifyStatic(LogisticVendorMapper.class, times(1));
        LogisticVendorMapper.toLogisticVendor(LogisticVendorFleetUtil.logisticVendorFleetLogisticVendorOnly);
    }

    @Test
    public void toLogisticVendorList() {
        given(LogisticVendorMapper.toLogisticVendorList(LogisticVendorFleetUtil.logisticVendorFleetListLogisticVendorOnly))
                .willReturn(LogisticVendorUtil.logisticVendorListCompleteAttribute);
        List<LogisticVendor> expectedResponse = LogisticVendorMapper.toLogisticVendorList(LogisticVendorFleetUtil.logisticVendorFleetListLogisticVendorOnly);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(LogisticVendorUtil.logisticVendorListCompleteAttribute));
        verifyStatic(LogisticVendorMapper.class, times(1));
        LogisticVendorMapper.toLogisticVendorList(LogisticVendorFleetUtil.logisticVendorFleetListLogisticVendorOnly);
    }
}