package com.gdn.controller.cff;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.*;
import com.gdn.cff.CffService;
import com.gdn.entity.AllowedVehicle;
import com.gdn.entity.Cff;
import com.gdn.entity.CffGood;
import com.gdn.mapper.CffResponseMapper;
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
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(CffController.class)
public class CffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CffService cffService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAllCff_OK() throws Exception {
        given(cffService.getAllCff())
                .willReturn(WebResponse.OK(CffResponseUtil.cffResponseListCompleteAttribute));
        mockMvc.perform(get("/api/cff")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data[0].merchantName", equalTo(CffUtil.cffCompleteAttribute.getMerchant().getName())))
                .andExpect(jsonPath("$.data[0].cffId", equalTo(CffUtil.cffCompleteAttribute.getId())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].id", equalTo(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getId())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].sku", equalTo(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getSku())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].length", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getLength())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].width", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getWidth())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].height", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getHeight())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].weight", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getWeight())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].cbm", equalTo(new BigDecimal(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getCbm()).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue())))
                .andExpect(jsonPath("$.data[0].cffGoodList[0].quantity", equalTo(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getQuantity())));
        verify(cffService, times(1)).getAllCff();
    }

    @Test
    public void getOneCff_OK() throws Exception {
        given(cffService.getOneCff(CffUtil.cffCompleteAttribute.getId()))
                .willReturn(WebResponse.OK(CffResponseUtil.cffResponseCompleteAttribute));
        mockMvc.perform(get("/api/cff/{id}", CffUtil.cffCompleteAttribute.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.merchantName", equalTo(CffUtil.cffCompleteAttribute.getMerchant().getName())))
                .andExpect(jsonPath("$.data.cffId", equalTo(CffUtil.cffCompleteAttribute.getId())))
                .andExpect(jsonPath("$.data.cffGoodList[0].id", equalTo(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getId())))
                .andExpect(jsonPath("$.data.cffGoodList[0].sku", equalTo(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getSku())))
                .andExpect(jsonPath("$.data.cffGoodList[0].length", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getLength())))
                .andExpect(jsonPath("$.data.cffGoodList[0].width", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getWidth())))
                .andExpect(jsonPath("$.data.cffGoodList[0].height", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getHeight())))
                .andExpect(jsonPath("$.data.cffGoodList[0].weight", equalTo((double) CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getWeight())))
                .andExpect(jsonPath("$.data.cffGoodList[0].cbm", equalTo(new BigDecimal(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getCbm()).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue())))
                .andExpect(jsonPath("$.data.cffGoodList[0].quantity", equalTo(CffUtil.cffCompleteAttribute.getCffGoodList().get(0).getQuantity())));
        verify(cffService, times(1)).getOneCff(CffUtil.cffCompleteAttribute.getId());
    }

    @Test
    public void getOneCff_NOTFOUND() throws Exception {
        String cffIdNotExists = "cff id not exists";
        given(cffService.getOneCff(cffIdNotExists))
                .willReturn(WebResponse.NOT_FOUND());
        mockMvc.perform(get("/api/cff/{id}", cffIdNotExists)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.status", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", equalTo("Not Found")))
                .andExpect(jsonPath("$.data", nullValue()));
        verify(cffService, times(1)).getOneCff(cffIdNotExists);
    }

    // TODO : ENTAH KENAPA INI TEST GAK BISA -___-
    @Test
    public void saveCff_OK() throws Exception {
        Cff uploadCff = CffUtil.uploadCffExistingMerchantExistingPickupPoint;
        uploadCff.setUploadedDate(new Date());
        uploadCff.setSchedulingStatus(SchedulingStatus.PENDING);
        uploadCff.getMerchant().setId(MerchantUtil.merchantCompleteAttribute.getId());
        for (CffGood cffGood:uploadCff.getCffGoodList()
                ) {
            cffGood.setId("cff_good_" + UUID.randomUUID().toString());
//            cffGood.setCff(uploadCff);
        }
        uploadCff.getPickupPoint().setId(PickupPointUtil.pickupPointCompleteAttribute.getId());
        for (AllowedVehicle allowedVehicle:uploadCff.getPickupPoint().getAllowedVehicleList()){
            allowedVehicle.setId("allowed_vehicle_" + UUID.randomUUID().toString());
//            allowedVehicle.setPickupPoint(uploadCff.getPickupPoint());
        }
        String request = objectMapper.writeValueAsString(uploadCff);
        System.out.println(request);
        given(cffService.saveCff(CffUtil.uploadCffExistingMerchantExistingPickupPoint))
                .willReturn(WebResponse.OK(CffResponseMapper.toCffResponse(uploadCff)));
        mockMvc.perform(post("/api/cff")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.merchantName", equalTo(CffUtil.uploadCffExistingMerchantExistingPickupPoint.getMerchant().getName())))
                .andExpect(jsonPath("$.data.cffId", equalTo(CffUtil.uploadCffExistingMerchantExistingPickupPoint.getId())))
                .andExpect(jsonPath("$.data.cffGoodList[0].id", equalTo(CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getId())))
                .andExpect(jsonPath("$.data.cffGoodList[0].sku", equalTo(CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getSku())))
                .andExpect(jsonPath("$.data.cffGoodList[0].length", equalTo((double) CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getLength())))
                .andExpect(jsonPath("$.data.cffGoodList[0].width", equalTo((double) CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getWidth())))
                .andExpect(jsonPath("$.data.cffGoodList[0].height", equalTo((double) CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getHeight())))
                .andExpect(jsonPath("$.data.cffGoodList[0].weight", equalTo((double) CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getWeight())))
                .andExpect(jsonPath("$.data.cffGoodList[0].cbm", equalTo(new BigDecimal(CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getCbm()).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue())))
                .andExpect(jsonPath("$.data.cffGoodList[0].quantity", equalTo(CffUtil.uploadCffExistingMerchantExistingPickupPoint.getCffGoodList().get(0).getQuantity())));
        verify(cffService, times(1)).saveCff(uploadCff);
    }
}