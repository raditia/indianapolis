package com.gdn.recommendation_algorithm.implementation;

import com.gdn.FleetUtil;
import com.gdn.PickupUtil;
import com.gdn.ProductUtil;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PickupProcessorImplTest {

    @Mock
    private FleetProcessorService fleetProcessorService;

    @InjectMocks
    private PickupProcessorImpl pickupProcessor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNextPickupFalse(){
        given(fleetProcessorService.getNextFleetWillUsed(ProductUtil.productList,FleetUtil.fleetVanCompleteAttribute)).willReturn(FleetUtil.fleetMotorCompleteAttribute);

        Pickup pickup = pickupProcessor.getNextPickup(ProductUtil.productList,FleetUtil.fleetVanCompleteAttribute);

        assertThat(pickup, notNullValue());
        assertThat(pickup, equalTo(PickupUtil.pickup));

        verify(fleetProcessorService, times(1)).getNextFleetWillUsed(ProductUtil.productList,FleetUtil.fleetVanCompleteAttribute);
    }

    @Test
    public void getNextPickupTrue(){
        given(fleetProcessorService.getNextFleetWillUsed(ProductUtil.productList2,FleetUtil.fleetMotorCompleteAttribute)).willReturn(FleetUtil.fleetMotorCompleteAttribute);

        Pickup pickup = pickupProcessor.getNextPickup(ProductUtil.productList2,FleetUtil.fleetMotorCompleteAttribute);

        assertThat(pickup, notNullValue());
        assertThat(pickup, equalTo(PickupUtil.pickup1));

        verify(fleetProcessorService, times(1)).getNextFleetWillUsed(ProductUtil.productList2,FleetUtil.fleetMotorCompleteAttribute);
    }
}