package com.gdn.cff.implementation;

import com.gdn.*;
import com.gdn.entity.*;
import com.gdn.mapper.CffMapper;
import com.gdn.mapper.CffResponseMapper;
import com.gdn.repository.CffRepository;
import com.gdn.repository.MerchantRepository;
import com.gdn.repository.PickupPointRepository;
import com.gdn.request.CffRequest;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CffMapper.class)
public class CffServiceImplTest {

    @Mock
    private CffRepository cffRepository;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private PickupPointRepository pickupPointRepository;

    @InjectMocks
    private CffServiceImpl cffService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockStatic(CffMapper.class);
    }

    @Test
    public void getAllCffExists() {
        given(cffRepository.findAllByOrderByWarehouseAsc()).willReturn(CffUtil.cffListCompleteAttribute);

        WebResponse<List<CffResponse>> expectedResponse = cffService.getAllCff();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(CffResponseUtil.cffResponseListCompleteAttribute)));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(cffRepository, times(1)).findAllByOrderByWarehouseAsc();
    }

    @Test
    public void getOneCffExists() {
        given(cffRepository.getOne(CffUtil.cffCompleteAttribute.getId())).willReturn(CffUtil.cffCompleteAttribute);

        WebResponse<CffResponse> expectedResponse = cffService.getOneCff(CffUtil.cffCompleteAttribute.getId());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(CffResponseUtil.cffResponseCompleteAttribute)));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(cffRepository, times(1)).getOne(CffUtil.cffCompleteAttribute.getId());
    }

    @Test
    public void getOneCffNotExists() {
        given(cffRepository.getOne("cff id not exists")).willThrow(EntityNotFoundException.class);

        WebResponse<CffResponse> expectedResponse = cffService.getOneCff("cff id not exists");

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.NOT_FOUND()));

        verify(cffRepository, times(1)).getOne("cff id not exists");
    }

    @Test
    public void updateSchedulingStatusSuccess() {
        Cff existingCffInDb = CffUtil.cffCompleteAttribute;
        given(cffRepository.getOne(existingCffInDb.getId())).willReturn(existingCffInDb);
        existingCffInDb.setSchedulingStatus(SchedulingStatus.DONE);
        given(cffRepository.save(existingCffInDb)).willReturn(existingCffInDb);

        Cff expectedResponse = cffService.updateSchedulingStatus(existingCffInDb.getId());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(existingCffInDb));

        verify(cffRepository, times(1)).getOne(existingCffInDb.getId());
        verify(cffRepository, times(1)).save(existingCffInDb);
    }

    // Disini tetap memakai CffResponseMapper (gak pakai util), alasannya adalah
    // karena di CffService itu generate random UUID untuk cffGood dan allowedVehicle
    // Assert-nya sulit karena kan random UUID itu
    @Test
    public void saveCffNewMerchantNewPickupPoint() {
        CffRequest cffRequest = CffRequestUtil.cffRequestCompleteAttributeNewMerchantNewPickupPoint;
        Cff uploadCff = CffUtil.uploadCffNewMerchantNewPickupPoint;
        given(CffMapper.toCff(CffRequestUtil.cffRequestCompleteAttributeNewMerchantNewPickupPoint))
                .willReturn(CffUtil.uploadCffNewMerchantNewPickupPoint);
        uploadCff.setUploadedDate(new Date());

        given(merchantRepository
                .findByEmailAddress(MerchantUtil.newMerchantUploadCff.getEmailAddress()))
                .willReturn(null);
        String newMerchantId = CffResponseUtil.newMerchantId;
        uploadCff.getMerchant().setId(newMerchantId);

        uploadCff.setSchedulingStatus(SchedulingStatus.PENDING);

        for (CffGood cffGood:uploadCff.getCffGoodList()){
            cffGood.setId("cff_good_" + UUID.randomUUID().toString());
            cffGood.setCff(uploadCff);
        }

        given(pickupPointRepository
                .findByPickupAddressAndLatitudeAndLongitude(
                        PickupPointUtil.newPickupPointUploadCff.getPickupAddress(),
                        PickupPointUtil.newPickupPointUploadCff.getLatitude(),
                        PickupPointUtil.newPickupPointUploadCff.getLongitude()))
                .willReturn(null);
        String newPickupPointId = CffResponseUtil.newPickupPointId;
        uploadCff.getPickupPoint().setId(newPickupPointId);

        for (AllowedVehicle allowedVehicle:uploadCff.getPickupPoint().getAllowedVehicleList()
                ) {
            allowedVehicle.setId("allowed_vehicle_" + UUID.randomUUID().toString());
            allowedVehicle.setPickupPoint(uploadCff.getPickupPoint());
        }

        given(cffRepository.save(uploadCff)).willReturn(uploadCff);

        WebResponse<CffResponse> expectedResponse = cffService.saveCff(cffRequest);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(CffResponseMapper.toCffResponse(uploadCff))));

        InOrder inOrder = Mockito.inOrder(merchantRepository, pickupPointRepository, cffRepository);
        inOrder.verify(merchantRepository, times(1))
                .findByEmailAddress(MerchantUtil.newMerchantUploadCff.getEmailAddress());
        inOrder.verify(pickupPointRepository, times(1))
                .findByPickupAddressAndLatitudeAndLongitude(
                        PickupPointUtil.newPickupPointUploadCff.getPickupAddress(),
                        PickupPointUtil.newPickupPointUploadCff.getLatitude(),
                        PickupPointUtil.newPickupPointUploadCff.getLongitude());
        inOrder.verify(cffRepository, times(1)).save(uploadCff);
    }

    @Test
    public void saveCffExistingMerchantExistingPickupPoint() {
        CffRequest cffRequest = CffRequestUtil.cffRequestCompleteAttributeExistingMerchantExistingPickupPoint;
        Cff uploadCff = CffUtil.uploadCffExistingMerchantExistingPickupPoint;
        given(CffMapper.toCff(CffRequestUtil.cffRequestCompleteAttributeExistingMerchantExistingPickupPoint))
                .willReturn(CffUtil.uploadCffExistingMerchantExistingPickupPoint);
        uploadCff.setUploadedDate(new Date());

        given(merchantRepository
                .findByEmailAddress(MerchantUtil.existingMerchantUploadCff.getEmailAddress()))
                .willReturn(MerchantUtil.merchantCompleteAttribute);
        uploadCff.getMerchant().setId(MerchantUtil.merchantCompleteAttribute.getId());

        uploadCff.setSchedulingStatus(SchedulingStatus.PENDING);

        for (CffGood cffGood:uploadCff.getCffGoodList()){
            cffGood.setId("cff_good_" + UUID.randomUUID().toString());
            cffGood.setCff(uploadCff);
        }

        given(pickupPointRepository
                .findByPickupAddressAndLatitudeAndLongitude(
                        PickupPointUtil.existingPickupPointUploadCff.getPickupAddress(),
                        PickupPointUtil.existingPickupPointUploadCff.getLatitude(),
                        PickupPointUtil.existingPickupPointUploadCff.getLongitude()))
                .willReturn(PickupPointUtil.pickupPointCompleteAttribute);
        uploadCff.getPickupPoint().setId(PickupPointUtil.existingPickupPointUploadCff.getId());

        for (AllowedVehicle allowedVehicle:uploadCff.getPickupPoint().getAllowedVehicleList()
                ) {
            allowedVehicle.setId(allowedVehicle.getId());
            allowedVehicle.setPickupPoint(uploadCff.getPickupPoint());
        }

        given(cffRepository.save(uploadCff)).willReturn(uploadCff);

        WebResponse<CffResponse> expectedResponse = cffService.saveCff(cffRequest);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(CffResponseMapper.toCffResponse(uploadCff))));

        InOrder inOrder = Mockito.inOrder(merchantRepository, pickupPointRepository, cffRepository);
        inOrder.verify(merchantRepository, times(1))
                .findByEmailAddress(MerchantUtil.existingMerchantUploadCff.getEmailAddress());
        inOrder.verify(pickupPointRepository, times(1))
                .findByPickupAddressAndLatitudeAndLongitude(
                        PickupPointUtil.existingPickupPointUploadCff.getPickupAddress(),
                        PickupPointUtil.existingPickupPointUploadCff.getLatitude(),
                        PickupPointUtil.existingPickupPointUploadCff.getLongitude());
        inOrder.verify(cffRepository, times(1)).save(uploadCff);
    }

    @After
    public void tearDown() throws Exception {
    }
}