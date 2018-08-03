package com.gdn.warehouse_category.implementation;

import com.gdn.entity.Category;
import com.gdn.entity.Warehouse;
import com.gdn.entity.WarehouseCategory;
import com.gdn.mapper.WarehouseCategoryResponseMapper;
import com.gdn.repository.WarehouseCategoryRepository;
import com.gdn.response.WarehouseCategoryResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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

    private Warehouse warehouse = Warehouse.builder()
            .id("warehouse_cawang")
            .address("cawang")
            .build();
    private Category category = Category.builder()
            .id("category_elektronik")
            .name("elektronik")
            .build();
    private WarehouseCategory warehouseCategory = WarehouseCategory.builder()
            .id("warehouse_cawang")
            .category(category)
            .warehouse(warehouse)
            .build();
    private List<WarehouseCategory> warehouseCategoryList = new ArrayList<WarehouseCategory>(){{
        add(warehouseCategory);
    }};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllWarehouseCategorySuccess() {
        given(warehouseCategoryRepository.findAll()).willReturn(warehouseCategoryList);

        WebResponse<List<WarehouseCategoryResponse>> expectedResponse = warehouseCategoryService.findAllWarehouseCategory();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(WarehouseCategoryResponseMapper.toWarehouseCategoryResponseList(warehouseCategoryList))));
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