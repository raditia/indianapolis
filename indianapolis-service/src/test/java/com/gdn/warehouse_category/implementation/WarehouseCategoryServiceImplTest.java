package com.gdn.warehouse_category.implementation;

import com.gdn.WarehouseCategoryResponseUtil;
import com.gdn.WarehouseCategoryUtil;
import com.gdn.repository.WarehouseCategoryRepository;
import com.gdn.response.WarehouseCategoryResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class WarehouseCategoryServiceImplTest {

    @Mock
    private WarehouseCategoryRepository warehouseCategoryRepository;

    @InjectMocks
    private WarehouseCategoryServiceImpl warehouseCategoryService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllWarehouseCategorySuccess() {
        given(warehouseCategoryRepository.findAll()).willReturn(WarehouseCategoryUtil.warehouseCategoryListCompleteAttribute);

        WebResponse<List<WarehouseCategoryResponse>> expectedResponse = warehouseCategoryService.findAllWarehouseCategory();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(WarehouseCategoryResponseUtil.warehouseCategoryResponseListCompleteAttribute)));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(warehouseCategoryRepository, times(1)).findAll();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(warehouseCategoryRepository);
    }
}