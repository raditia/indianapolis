package com.gdn.cff.implementation;

import com.gdn.SchedulingStatus;
import com.gdn.entity.*;
import com.gdn.helper.DateHelper;
import com.gdn.mapper.CffResponseMapper;
import com.gdn.repository.CffRepository;
import com.gdn.repository.MerchantRepository;
import com.gdn.repository.PickupPointRepository;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CffServiceImplTest {

    @Mock
    private CffRepository cffRepository;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private PickupPointRepository pickupPointRepository;

    @InjectMocks
    private CffServiceImpl cffService;

    private CffGood cffGood1 = CffGood.builder()
            .id("1")
            .sku("sku1")
            .cbm(0.0f)
            .length(0.0f)
            .width(0.0f)
            .height(0.0f)
            .weight(0.0f)
            .build();
    private CffGood cffGood2 = CffGood.builder()
            .id("2")
            .sku("sku2")
            .cbm(0.0f)
            .length(0.0f)
            .width(0.0f)
            .height(0.0f)
            .weight(0.0f)
            .build();
    private List<CffGood> cffGoodList = new ArrayList<CffGood>(){{
        add(cffGood1);
        add(cffGood2);
    }};

    private Merchant merchant = Merchant.builder()
            .id("1")
            .name("merchant")
            .build();

    private UserRole userRole = UserRole.builder()
            .id("1")
            .build();
    private User user = User.builder()
            .id("1")
            .userRole(userRole)
            .build();

    private Warehouse warehouse = Warehouse.builder()
            .id("1")
            .address("cawang")
            .build();

    private AllowedVehicle allowedVehicle = AllowedVehicle.builder()
            .id("1")
            .build();
    private List<AllowedVehicle> allowedVehicleList = new ArrayList<AllowedVehicle>(){{
        add(allowedVehicle);
    }};

    private PickupPoint pickupPoint = PickupPoint.builder()
            .id("1")
            .allowedVehicleList(allowedVehicleList)
            .build();

    private Cff cff = Cff.builder()
            .id("1")
            .uploadedDate(new Date())
            .pickupDate(DateHelper.tomorrow())
            .cffGoodList(cffGoodList)
            .merchant(merchant)
            .warehouse(warehouse)
            .pickupPoint(pickupPoint)
            .tp(user)
            .schedulingStatus(SchedulingStatus.PENDING)
            .build();
    private List<Cff> cffList = new ArrayList<Cff>(){{
        add(cff);
    }};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllCffExists() {
        given(cffRepository.findAllByOrderByWarehouseAsc()).willReturn(cffList);

        WebResponse<List<CffResponse>> expectedResponse = cffService.getAllCff();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(CffResponseMapper.toCffListResponse(cffList))));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(cffRepository, times(1)).findAllByOrderByWarehouseAsc();
    }

    @Test
    public void getOneCffExists() {
        given(cffRepository.getOne("1")).willReturn(cff);

        WebResponse<CffResponse> expectedResponse = cffService.getOneCff("1");

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(CffResponseMapper.toCffResponse(cff))));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(cffRepository, times(1)).getOne("1");
    }

    @After
    public void tearDown() throws Exception {
    }
}