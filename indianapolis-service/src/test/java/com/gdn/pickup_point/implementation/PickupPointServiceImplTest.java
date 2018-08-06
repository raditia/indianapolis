package com.gdn.pickup_point.implementation;

import com.gdn.PickupPointUtil;
import com.gdn.entity.PickupPoint;
import com.gdn.repository.PickupPointRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByPickupAddressOrLatitudeAndLongitude_OnlyAddressExists() {
        given(pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude(
                PickupPointUtil.pickupPointCompleteAttribute.getPickupAddress(),
                100.0,
                100.0)).willReturn(PickupPointUtil.pickupPointCompleteAttribute);

        PickupPoint expectedResponse = pickupPointService.findByPickupAddressOrLatitudeAndLongitude(PickupPointUtil.pickupPointCompleteAttribute.getPickupAddress(),
                100.0,
                100.0);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupPointUtil.pickupPointCompleteAttribute));

        verify(pickupPointRepository, times(1)).findByPickupAddressOrLatitudeAndLongitude(PickupPointUtil.pickupPointCompleteAttribute.getPickupAddress(),
                100.0,
                100.0);
    }

    @Test
    public void findByPickupAddressOrLatitudeAndLongitude_OnlyLatLngExists() {
        given(pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude("alamat",
                PickupPointUtil.pickupPointCompleteAttribute.getLatitude(),
                PickupPointUtil.pickupPointCompleteAttribute.getLongitude())).willReturn(PickupPointUtil.pickupPointCompleteAttribute);

        PickupPoint expectedResponse = pickupPointService.findByPickupAddressOrLatitudeAndLongitude("alamat",
                PickupPointUtil.pickupPointCompleteAttribute.getLatitude(),
                PickupPointUtil.pickupPointCompleteAttribute.getLongitude());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(PickupPointUtil.pickupPointCompleteAttribute));

        verify(pickupPointRepository, times(1)).findByPickupAddressOrLatitudeAndLongitude("alamat",
                PickupPointUtil.pickupPointCompleteAttribute.getLatitude(),
                PickupPointUtil.pickupPointCompleteAttribute.getLongitude());
    }

    @Test
    public void findByPickupAddressOrLatitudeAndLongitude_AddressAndLatLngNotExists() {
        given(pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude("alamat", 100.0, 100.0)).willReturn(null);

        PickupPoint expectedResponse = pickupPointService.findByPickupAddressOrLatitudeAndLongitude("alamat", 100.0, 100.0);

        assertThat(expectedResponse, nullValue());

        verify(pickupPointRepository, times(1)).findByPickupAddressOrLatitudeAndLongitude("alamat", 100.0, 100.0);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(pickupPointRepository);
    }
}