package com.gdn.controller.warehouse_category;

import com.gdn.WarehouseCategoryResponseUtil;
import com.gdn.WarehouseCategoryUtil;
import com.gdn.response.WebResponse;
import com.gdn.warehouse_category.WarehouseCategoryService;
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
@WebMvcTest(WarehouseCategoryController.class)
public class WarehouseCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseCategoryService warehouseCategoryService;

    @Test
    public void findAllWarehouseCategory_OK() throws Exception {
        given(warehouseCategoryService.findAllWarehouseCategory())
                .willReturn(WebResponse.OK(WarehouseCategoryResponseUtil.warehouseCategoryResponseListCompleteAttribute));
        mockMvc.perform(get("/api/warehouse-category")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data[0].warehouseId", equalTo(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getWarehouse().getId())))
                .andExpect(jsonPath("$.data[0].warehouseAddress", equalTo(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getWarehouse().getAddress())))
                .andExpect(jsonPath("$.data[0].categoryId", equalTo(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getCategory().getId())))
                .andExpect(jsonPath("$.data[0].categoryName", equalTo(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getCategory().getName())));
        verify(warehouseCategoryService, times(1)).findAllWarehouseCategory();
    }
}