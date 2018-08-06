package com.gdn.controller.category;

import com.gdn.CategoryResponseUtil;
import com.gdn.CategoryUtil;
import com.gdn.category.CategoryService;
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
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void findAllCategory_OK() throws Exception {
        given(categoryService.findAllCategory())
                .willReturn(WebResponse.OK(CategoryResponseUtil.categoryResponseListCompleteAttribute));
        mockMvc.perform(get("/api/category")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data[0].id", equalTo(CategoryUtil.categoryMinusWarehouseCategoryList.getId())))
                .andExpect(jsonPath("$.data[0].name", equalTo(CategoryUtil.categoryMinusWarehouseCategoryList.getName())));
        verify(categoryService, times(1)).findAllCategory();
    }
}