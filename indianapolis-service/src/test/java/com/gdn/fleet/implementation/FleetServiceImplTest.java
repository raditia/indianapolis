package com.gdn.fleet.implementation;

import com.gdn.entity.Fleet;
import com.gdn.mapper.FleetResponseMapper;
import com.gdn.repository.FleetRepository;
import com.gdn.response.FleetResponse;
import com.gdn.response.WebResponse;
import com.gdn.util.FleetUtil;
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

public class FleetServiceImplTest {

    @Mock
    private FleetRepository fleetRepository;

    @InjectMocks
    private FleetServiceImpl fleetService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllByOrderByCbmCapacityDesc() {
        given(fleetRepository.findAllByOrderByCbmCapacityDesc()).willReturn(FleetUtil.descendingFleetListCompleteAttribute);

        List<Fleet> expectedResponse = fleetService.findAllByOrderByCbmCapacityDesc();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(FleetUtil.descendingFleetListCompleteAttribute));

        verify(fleetRepository, times(1)).findAllByOrderByCbmCapacityDesc();
    }

    @Test
    public void findAllByOrderByCbmCapacityAsc() {
        given(fleetRepository.findAllByOrderByCbmCapacityAsc()).willReturn(FleetUtil.ascendingFleetListCompleteAttribute);

        List<Fleet> expectedResponse = fleetService.findAllByOrderByCbmCapacityAsc();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(FleetUtil.ascendingFleetListCompleteAttribute));

        verify(fleetRepository, times(1)).findAllByOrderByCbmCapacityAsc();
    }

    @Test
    public void findDistinctAllFleetOrderByCbmCapacityDesc() {
        given(fleetRepository.findDistinctByNameOrderByCbmCapacityDesc()).willReturn(FleetUtil.descendingFleetListCompleteAttribute);

        WebResponse<List<FleetResponse>> expectedResponse = fleetService.findDistinctAllFleetOrderByCbmCapacityDesc();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(FleetResponseMapper.toFleetResponseList(FleetUtil.descendingFleetListCompleteAttribute))));

        verify(fleetRepository, times(1)).findDistinctByNameOrderByCbmCapacityDesc();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(fleetRepository);
    }
}