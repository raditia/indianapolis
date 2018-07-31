package com.gdn.fleet.implementation;

import com.gdn.entity.Fleet;
import com.gdn.entity.LogisticVendor;
import com.gdn.mapper.FleetResponseMapper;
import com.gdn.repository.FleetRepository;
import com.gdn.response.FleetResponse;
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

public class FleetServiceImplTest {

    @Mock
    private FleetRepository fleetRepository;

    @InjectMocks
    private FleetServiceImpl fleetService;

    private LogisticVendor bes = LogisticVendor.builder()
            .id("bes")
            .emailAddress("email bes")
            .name("blibli express service")
            .phoneNumber("telp")
            .build();
    private Fleet motor = Fleet.builder()
            .id("1")
            .name("motor")
            .cbmCapacity(0.5f)
            .logisticVendor(bes)
            .minCbm(0f)
            .price(0.0)
            .build();
    private Fleet van = Fleet.builder()
            .id("2")
            .name("van")
            .cbmCapacity(1.5f)
            .minCbm(0.51f)
            .price(0.0)
            .build();

    private List<Fleet> descendingFleetList = new ArrayList<Fleet>(){{
        add(van);
        add(motor);
    }};
    private List<Fleet> ascendingFleetList = new ArrayList<Fleet>(){{
        add(motor);
        add(van);
    }};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllByOrderByCbmCapacityDesc() {
        given(fleetRepository.findAllByOrderByCbmCapacityDesc()).willReturn(descendingFleetList);

        List<Fleet> expectedResponse = fleetService.findAllByOrderByCbmCapacityDesc();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(descendingFleetList));

        verify(fleetRepository, times(1)).findAllByOrderByCbmCapacityDesc();
    }

    @Test
    public void findAllByOrderByCbmCapacityAsc() {
        given(fleetRepository.findAllByOrderByCbmCapacityAsc()).willReturn(ascendingFleetList);

        List<Fleet> expectedResponse = fleetService.findAllByOrderByCbmCapacityAsc();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(ascendingFleetList));

        verify(fleetRepository, times(1)).findAllByOrderByCbmCapacityAsc();
    }

    @Test
    public void findDistinctAllFleetOrderByCbmCapacityDesc() {
        given(fleetRepository.findDistinctByNameOrderByCbmCapacityDesc()).willReturn(descendingFleetList);

        WebResponse<List<FleetResponse>> expectedResponse = fleetService.findDistinctAllFleetOrderByCbmCapacityDesc();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(FleetResponseMapper.toFleetResponseList(descendingFleetList))));

        verify(fleetRepository, times(1)).findDistinctByNameOrderByCbmCapacityDesc();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(fleetRepository);
    }
}