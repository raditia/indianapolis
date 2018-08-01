package com.gdn.merchant.implementation;

import com.gdn.entity.Merchant;
import com.gdn.mapper.MerchantResponseMapper;
import com.gdn.repository.MerchantRepository;
import com.gdn.response.MerchantResponse;
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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    private Merchant merchant = Merchant.builder()
            .id("id")
            .name("merchant")
            .emailAddress("email merchant")
            .phoneNumber("telp")
            .build();
    private List<Merchant> merchantList = new ArrayList<Merchant>(){{
        add(merchant);
    }};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllMerchantExists() {
        given(merchantRepository.findAll()).willReturn(merchantList);

        WebResponse<List<MerchantResponse>> expectedResponse = merchantService.getAllMerchant();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(MerchantResponseMapper.toMerchantResponseList(merchantList))));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(merchantRepository, times(1)).findAll();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(merchantRepository);
    }

    @Test
    public void getAllMerchantNotExists() {

        // Not yet implemented in the real code

//        given(merchantRepository.findAll()).willReturn(new ArrayList<>());
//
//        WebResponse<List<MerchantResponse>> expectedResponse = merchantService.getAllMerchant();
//
//        assertThat(expectedResponse, notNullValue());
//        assertThat(expectedResponse.getData().isEmpty(), equalTo(true));
//        assertThat(expectedResponse.getData(), nullValue());
//        assertThat(expectedResponse, equalTo(WebResponse.NOT_FOUND()));
//        assertThat(expectedResponse.getCode(), equalTo(404));
//        assertThat(expectedResponse.getStatus(), equalTo("Not Found"));
//        assertThat(expectedResponse.getMessage(), equalTo("Not Found"));
    }

    @Test
    public void getOneExists() {
        given(merchantRepository.findByEmailAddress("email merchant")).willReturn(merchant);

        Merchant expectedResponse = merchantService.getOne("email merchant");

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(merchant));

        verify(merchantRepository, times(1)).findByEmailAddress("email merchant");
    }

    @Test
    public void getOneNotExists() {
        given(merchantRepository.findByEmailAddress("email tidak ada")).willReturn(null);

        Merchant expectedResponse = merchantService.getOne("email tidak ada");

        assertThat(expectedResponse, nullValue());
        verify(merchantRepository, times(1)).findByEmailAddress("email tidak ada");
    }


}