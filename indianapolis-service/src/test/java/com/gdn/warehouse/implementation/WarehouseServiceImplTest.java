package com.gdn.warehouse.implementation;

import com.gdn.entity.Warehouse;
import com.gdn.mapper.WarehouseResponseMapper;
import com.gdn.repository.WarehouseRepository;
import com.gdn.response.WarehouseResponse;
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

public class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private List<Warehouse> warehouseList = new ArrayList<Warehouse>(){{
        add(Warehouse.builder()
                .id("warehouse_cawang")
                .address("cawang")
                .build());
        add(Warehouse.builder()
                .id("warehouse_cakung")
                .address("cakung")
                .build());
    }};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllWarehouse() {
        given(warehouseRepository.findAll()).willReturn(warehouseList);

        WebResponse<List<WarehouseResponse>> expectedResponse = warehouseService.findAllWarehouse();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(WarehouseResponseMapper.toWarehouseResponseList(warehouseList))));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(warehouseRepository, times(1)).findAll();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(warehouseRepository);
    }
}