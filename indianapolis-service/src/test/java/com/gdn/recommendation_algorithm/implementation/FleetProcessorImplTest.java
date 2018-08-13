package com.gdn.recommendation_algorithm.implementation;

import com.gdn.*;
import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class FleetProcessorImplTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private FleetProcessorImpl fleetProcessor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTopThreeFleetsWillUsed() {
        given(fleetService.findAllByOrderByCbmCapacityDesc()).willReturn(FleetUtil.descendingFleetListCompleteAttribute);

        List<Fleet> topThreeFleetsWillUsed = fleetProcessor.getTopThreeFleetsWillUsed(DatabaseQueryResultUtil.databaseQueryResultList);

        assertThat(topThreeFleetsWillUsed, notNullValue());
        assertThat(topThreeFleetsWillUsed.isEmpty(), equalTo(false));
        assertThat(topThreeFleetsWillUsed, equalTo(FleetUtil.topFleetsWillUsed));

        verify(fleetService, times(2)).findAllByOrderByCbmCapacityDesc();
    }

    @Test
    public void getTopThreeFleetsWillUsedMoreThanThree() {
        given(fleetService.findAllByOrderByCbmCapacityDesc()).willReturn(FleetUtil.descendingFleetListCompleteAttribute1);

        List<Fleet> topThreeFleetsWillUsed = fleetProcessor.getTopThreeFleetsWillUsed(DatabaseQueryResultUtil.databaseQueryResultList1);

        assertThat(topThreeFleetsWillUsed, notNullValue());
        assertThat(topThreeFleetsWillUsed.isEmpty(), equalTo(false));
        assertThat(topThreeFleetsWillUsed, equalTo(FleetUtil.topFleetsWillUsed1));

        verify(fleetService, times(2)).findAllByOrderByCbmCapacityDesc();
    }

    @Test
    public void getNextFleetWillUsedTrue() {
        given(fleetService.findAllByOrderByCbmCapacityDesc()).willReturn(FleetUtil.descendingFleetListCompleteAttribute);

        Fleet nextFleetWillUsed = fleetProcessor.getNextFleetWillUsed(ProductUtil.productList, FleetUtil.fleetMotorCompleteAttribute);

        assertThat(nextFleetWillUsed, notNullValue());
        assertThat(nextFleetWillUsed, equalTo(FleetUtil.fleetMotorCompleteAttribute));

        verify(fleetService, times(1)).findAllByOrderByCbmCapacityDesc();
    }

    @Test
    public void getNextFleetWillUsedFalse() {
        given(fleetService.findAllByOrderByCbmCapacityDesc()).willReturn(FleetUtil.descendingFleetListCompleteAttribute);
        given(fleetService.findAllByOrderByCbmCapacityAsc()).willReturn(FleetUtil.ascendingFleetListCompleteAttribute);

        Fleet nextFleetWillUsed = fleetProcessor.getNextFleetWillUsed(ProductUtil.productList1, FleetUtil.fleetMotorCompleteAttribute);

        assertThat(nextFleetWillUsed, notNullValue());
        assertThat(nextFleetWillUsed, equalTo(FleetUtil.fleetVanCompleteAttribute));

        verify(fleetService, times(1)).findAllByOrderByCbmCapacityDesc();
    }

    @Test
    public void getFleetWithMoreCbmCapacity() {
        given(fleetService.findAllByOrderByCbmCapacityAsc()).willReturn(FleetUtil.ascendingFleetListCompleteAttribute);

        Fleet fleetWithMoreCbmCapacity = fleetProcessor.getFleetWithMoreCbmCapacity(FleetUtil.fleetMotorCompleteAttribute);

        assertThat(fleetWithMoreCbmCapacity, notNullValue());
        assertThat(fleetWithMoreCbmCapacity, equalTo(FleetUtil.fleetVanCompleteAttribute));

        verify(fleetService, times(1)).findAllByOrderByCbmCapacityAsc();
    }
}