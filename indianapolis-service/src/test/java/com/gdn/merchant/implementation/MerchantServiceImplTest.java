package com.gdn.merchant.implementation;

import com.gdn.MerchantResponseUtil;
import com.gdn.MerchantUtil;
import com.gdn.entity.Merchant;
import com.gdn.repository.MerchantRepository;
import com.gdn.response.MerchantResponse;
import com.gdn.response.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllMerchantExists() {
        given(merchantRepository.findAll()).willReturn(MerchantUtil.merchantListCompleteAttribute);

        WebResponse<List<MerchantResponse>> expectedResponse = merchantService.getAllMerchant();

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse.getData().isEmpty(), equalTo(false));
        assertThat(expectedResponse, equalTo(WebResponse.OK(MerchantResponseUtil.merchantResponseListCompleteAttribute)));
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
        given(merchantRepository.findByEmailAddress(MerchantUtil.merchantCompleteAttribute.getEmailAddress())).willReturn(MerchantUtil.merchantCompleteAttribute);

        Merchant expectedResponse = merchantService.getOne(MerchantUtil.merchantCompleteAttribute.getEmailAddress());

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(MerchantUtil.merchantCompleteAttribute));

        verify(merchantRepository, times(1)).findByEmailAddress(MerchantUtil.merchantCompleteAttribute.getEmailAddress());
    }

    @Test
    public void getOneNotExists() {
        given(merchantRepository.findByEmailAddress("email tidak ada")).willReturn(null);

        Merchant expectedResponse = merchantService.getOne("email tidak ada");

        assertThat(expectedResponse, nullValue());
        verify(merchantRepository, times(1)).findByEmailAddress("email tidak ada");
    }


}