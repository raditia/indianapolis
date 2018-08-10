package com.gdn.logistic_vendor.implementation;

import com.gdn.LogisticVendorResponseUtil;
import com.gdn.LogisticVendorUtil;
import com.gdn.repository.LogisticVendorRepository;
import com.gdn.response.LogisticVendorResponse;
import com.gdn.response.WebResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LogisticVendorServiceImplTest {

    @Mock
    private LogisticVendorRepository logisticVendorRepository;

    @InjectMocks
    private LogisticVendorServiceImpl logisticVendorService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll_OK() {
        given(logisticVendorRepository.findAll()).willReturn(LogisticVendorUtil.logisticVendorListCompleteAttribute);
        WebResponse<List<LogisticVendorResponse>> expectedResponse = logisticVendorService.findAll();
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(LogisticVendorResponseUtil.logisticVendorResponseList)));
        verify(logisticVendorRepository, times(1)).findAll();
    }
}