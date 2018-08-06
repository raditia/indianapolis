package com.gdn.controller.merchant;

import com.gdn.MerchantResponseUtil;
import com.gdn.merchant.MerchantService;
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
@WebMvcTest(MerchantController.class)
public class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantService merchantService;

    @Test
    public void findAllMerchant_OK() throws Exception {
        given(merchantService.getAllMerchant())
                .willReturn(WebResponse.OK(MerchantResponseUtil.merchantResponseListCompleteAttribute));
        mockMvc.perform(get("/api/merchant")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data[0].name", equalTo(MerchantResponseUtil.merchantResponseCompleteAttribute.getName())))
                .andExpect(jsonPath("$.data[0].emailAddress", equalTo(MerchantResponseUtil.merchantResponseCompleteAttribute.getEmailAddress())))
                .andExpect(jsonPath("$.data[0].phoneNumber", equalTo(MerchantResponseUtil.merchantResponseCompleteAttribute.getPhoneNumber())));
        verify(merchantService, times(1)).getAllMerchant();
    }
}