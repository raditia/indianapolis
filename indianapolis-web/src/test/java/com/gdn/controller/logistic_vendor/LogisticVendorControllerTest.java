package com.gdn.controller.logistic_vendor;

import com.gdn.LogisticVendorResponseUtil;
import com.gdn.logistic_vendor.LogisticVendorService;
import com.gdn.response.WebResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(LogisticVendorController.class)
public class LogisticVendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogisticVendorService logisticVendorService;

    @Test
    public void findAllLogisticVendor_OK() throws Exception {
        given(logisticVendorService.findAll()).willReturn(WebResponse.OK(LogisticVendorResponseUtil.logisticVendorResponseList));
        mockMvc.perform(get("/api/logistic-vendor")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data[0].id", equalTo(LogisticVendorResponseUtil.logisticVendorResponseList.get(0).getId())))
                .andExpect(jsonPath("$.data[0].name", equalTo(LogisticVendorResponseUtil.logisticVendorResponseList.get(0).getName())));
        verify(logisticVendorService, times(1)).findAll();
    }
}