package com.gdn.controller.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.*;
import com.gdn.pickup.PickupService;
import com.gdn.recommendation.RecommendationService;
import com.gdn.response.WebResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(RecommendationController.class)
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;
    @MockBean
    private PickupService pickupService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAllRecommendationFleetResult_OK() throws Exception {
        given(recommendationService.findAllRecommendationFleetResult(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId()))
                .willReturn(WebResponse.OK(RecommendationResponseUtil.recommendationResponseCompleteAttribute));
        mockMvc.perform(get("/api/recommendation/result")
                .param("warehouseId", WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.warehouseName", equalTo(RecommendationResponseUtil.recommendationResponseCompleteAttribute.getWarehouseName())))
                .andExpect(jsonPath("$.data.cbmTotal", equalTo(new BigDecimal(RecommendationResponseUtil.recommendationResponseCompleteAttribute.getCbmTotal()).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue())))
                .andExpect(jsonPath("$.data.fleetRecommendationResponseList[0].id", equalTo(RecommendationResponseUtil.recommendationResponseCompleteAttribute.getFleetRecommendationResponseList().get(0).getId())))
                .andExpect(jsonPath("$.data.fleetRecommendationResponseList[0].fleetName", equalTo(RecommendationResponseUtil.recommendationResponseCompleteAttribute.getFleetRecommendationResponseList().get(0).getFleetName())));
        verify(recommendationService, times(1)).findAllRecommendationFleetResult(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId());
    }

    @Test
    public void recommendation_OK() throws Exception {
        mockMvc.perform(get("/api/recommendation/execute")
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        verify(recommendationService, times(1)).executeBatch();
    }

    // TODO : Belum assert pickupDate
    @Test
    public void chooseRecommendationAndInsertToDatabase_OK() throws Exception {
        given(pickupService.savePickup(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute))
                .willReturn(WebResponse.OK(PickupChoiceResponseUtil.pickupChoiceResponseCompleteAttribute));
        mockMvc.perform(post("/api/recommendation/pickup")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute)))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.fleetList[0].fleetName", equalTo(PickupChoiceResponseUtil.pickupChoiceResponseCompleteAttribute.getFleetList().get(0).getFleetName())));
        verify(pickupService, times(1)).savePickup(PickupChoiceRequestUtil.pickupChoiceRequestCompleteAttribute);
    }
}