package com.gdn.login.implementation;

import com.gdn.mapper.LoginResponseMapper;
import com.gdn.repository.UserRepository;
import com.gdn.request.LoginRequest;
import com.gdn.response.LoginResponse;
import com.gdn.response.WebResponse;
import com.gdn.util.UserUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

public class LoginServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    private LoginRequest correctLoginRequest = LoginRequest.builder()
            .email("user 1 email")
            .password("user 1 password")
            .build();
    LoginRequest wrongLoginRequest = LoginRequest.builder()
            .email("email salah")
            .password("password salah")
            .build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loginWithEmailAndPasswordSuccess() {
        given(userRepository.findByEmailAddressAndPassword(correctLoginRequest.getEmail(), correctLoginRequest.getPassword())).willReturn(UserUtil.userCompleteAttribute);

        WebResponse<LoginResponse> expectedResponse = loginService.loginWithEmailAndPassword(correctLoginRequest);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(LoginResponseMapper.toLoginResponse(UserUtil.userCompleteAttribute))));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(userRepository, times(1)).findByEmailAddressAndPassword(correctLoginRequest.getEmail(), correctLoginRequest.getPassword());
    }

    @Test
    public void loginWithEmailAndPasswordFailed() {
        given(userRepository.findByEmailAddressAndPassword(wrongLoginRequest.getEmail(), wrongLoginRequest.getPassword())).willReturn(null);

        WebResponse<LoginResponse> expectedResponse = loginService.loginWithEmailAndPassword(wrongLoginRequest);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.NOT_FOUND()));
        assertThat(expectedResponse.getCode(), equalTo(404));
        assertThat(expectedResponse.getStatus(), equalTo("Not Found"));
        assertThat(expectedResponse.getMessage(), equalTo("Not Found"));
        assertThat(expectedResponse.getData(), nullValue());

        verify(userRepository, times(1)).findByEmailAddressAndPassword(wrongLoginRequest.getEmail(), wrongLoginRequest.getPassword());
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(userRepository);
    }
}