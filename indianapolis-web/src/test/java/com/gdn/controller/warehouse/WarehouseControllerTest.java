package com.gdn.controller.warehouse;

import com.gdn.WarehouseResponseUtil;
import com.gdn.WarehouseUtil;
import com.gdn.response.WebResponse;
import com.gdn.warehouse.WarehouseService;
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
@WebMvcTest(WarehouseController.class)
public class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @Test
    public void findAllWarehouse_OK() throws Exception {
        given(warehouseService.findAllWarehouse())
                .willReturn(WebResponse.OK(WarehouseResponseUtil.warehouseResponseListCompleteAttribute));
        mockMvc.perform(get("/api/warehouse")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data[0].id", equalTo(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())))
                .andExpect(jsonPath("$.data[0].address", equalTo(WarehouseUtil.warehouseMinusWarehouseCategoryList.getAddress())));
        verify(warehouseService, times(1)).findAllWarehouse();
    }
}