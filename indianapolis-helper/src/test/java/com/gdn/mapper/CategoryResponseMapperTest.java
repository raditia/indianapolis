package com.gdn.mapper;

import com.gdn.CategoryResponseUtil;
import com.gdn.CategoryUtil;
import com.gdn.response.CategoryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CategoryResponseMapper.class)
public class CategoryResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(CategoryResponseMapper.class);
    }

    @Test
    public void toCategoryResponse() {
        given(CategoryResponseMapper
                .toCategoryResponse(CategoryUtil.categoryMinusWarehouseCategoryList))
                .willReturn(CategoryResponseUtil.categoryResponseCompleteAttribute);
        CategoryResponse expectedResponse = CategoryResponseMapper.toCategoryResponse(CategoryUtil.categoryMinusWarehouseCategoryList);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(CategoryResponseUtil.categoryResponseCompleteAttribute));
        verifyStatic(CategoryResponseMapper.class, times(1));
        CategoryResponseMapper.toCategoryResponse(CategoryUtil.categoryMinusWarehouseCategoryList);
    }

    @Test
    public void toCategoryResponseList() {
        given(CategoryResponseMapper
                .toCategoryListResponse(CategoryUtil.categoryListMinusWarehouseCategory))
                .willReturn(CategoryResponseUtil.categoryResponseListCompleteAttribute);
        List<CategoryResponse> expectedResponse = CategoryResponseMapper.toCategoryListResponse(CategoryUtil.categoryListMinusWarehouseCategory);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(CategoryResponseUtil.categoryResponseListCompleteAttribute));
        verifyStatic(CategoryResponseMapper.class, times(1));
        CategoryResponseMapper.toCategoryListResponse(CategoryUtil.categoryListMinusWarehouseCategory);
    }
}