package com.gdn.pickup_point.implementation;

import com.gdn.entity.AllowedVehicle;
import com.gdn.entity.PickupPoint;
import com.gdn.repository.PickupPointRepository;
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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class PickupPointServiceImplTest {

    @Mock
    private PickupPointRepository pickupPointRepository;

    @InjectMocks
    private PickupPointServiceImpl pickupPointService;

    private AllowedVehicle allowedVehicle = AllowedVehicle.builder()
            .id("1")
            .vehicleName("motor")
            .build();
    private List<AllowedVehicle> allowedVehicleList = new ArrayList<AllowedVehicle>(){{
        add(allowedVehicle);
    }};
    private PickupPoint pickupPoint = PickupPoint.builder()
            .id("id")
            .pickupAddress("address")
            .latitude(0.0)
            .longitude(0.0)
            .allowedVehicleList(allowedVehicleList)
            .build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByPickupAddressOrLatitudeAndLongitude_OnlyAddressExists() {
        given(pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude("address", 1.5, 1.5)).willReturn(pickupPoint);

        PickupPoint expectedResponse = pickupPointService.findByPickupAddressOrLatitudeAndLongitude("address", 1.5, 1.5);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(pickupPoint));

        verify(pickupPointRepository, times(1)).findByPickupAddressOrLatitudeAndLongitude("address", 1.5, 1.5);
    }

    @Test
    public void findByPickupAddressOrLatitudeAndLongitude_OnlyLatLngExists() {
        given(pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude("alamat", 0.0, 0.0)).willReturn(pickupPoint);

        PickupPoint expectedResponse = pickupPointService.findByPickupAddressOrLatitudeAndLongitude("alamat", 0.0, 0.0);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(pickupPoint));

        verify(pickupPointRepository, times(1)).findByPickupAddressOrLatitudeAndLongitude("alamat", 0.0, 0.0);
    }

    @Test
    public void findByPickupAddressOrLatitudeAndLongitude_AddressAndLatLngNotExists() {
        given(pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude("alamat", 1.0, 1.0)).willReturn(null);

        PickupPoint expectedResponse = pickupPointService.findByPickupAddressOrLatitudeAndLongitude("alamat", 1.0, 1.0);

        assertThat(expectedResponse, nullValue());

        verify(pickupPointRepository, times(1)).findByPickupAddressOrLatitudeAndLongitude("alamat", 1.0, 1.0);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(pickupPointRepository);
    }
}