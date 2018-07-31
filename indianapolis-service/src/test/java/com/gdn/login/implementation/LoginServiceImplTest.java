package com.gdn.login.implementation;

import com.gdn.entity.User;
import com.gdn.entity.UserRole;
import com.gdn.mapper.LoginResponseMapper;
import com.gdn.repository.UserRepository;
import com.gdn.request.LoginRequest;
import com.gdn.response.LoginResponse;
import com.gdn.response.WebResponse;
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

    private UserRole userRole = UserRole.builder()
            .id("id")
            .role("role")
            .build();
    private User user = User.builder()
            .id("id")
            .name("axell")
            .emailAddress("email")
            .password("123")
            .userRole(userRole)
            .build();
    private LoginRequest loginRequest = LoginRequest.builder()
            .email("email")
            .password("123")
            .build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loginWithEmailAndPasswordSuccess() {
        given(userRepository.findByEmailAddressAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).willReturn(user);

        WebResponse<LoginResponse> expectedResponse = loginService.loginWithEmailAndPassword(loginRequest);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.OK(LoginResponseMapper.toLoginResponse(user))));
        assertThat(expectedResponse.getCode(), equalTo(200));
        assertThat(expectedResponse.getStatus(), equalTo("OK"));
        assertThat(expectedResponse.getMessage(), equalTo("OK"));

        verify(userRepository, times(1)).findByEmailAddressAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @Test
    public void loginWithEmailAndPasswordFailed() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("email salah")
                .password("password salah")
                .build();
        given(userRepository.findByEmailAddressAndPassword(loginRequest.getEmail(), loginRequest.getPassword())).willReturn(null);

        WebResponse<LoginResponse> expectedResponse = loginService.loginWithEmailAndPassword(loginRequest);

        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(WebResponse.NOT_FOUND()));
        assertThat(expectedResponse.getCode(), equalTo(404));
        assertThat(expectedResponse.getStatus(), equalTo("Not Found"));
        assertThat(expectedResponse.getMessage(), equalTo("Not Found"));
        assertThat(expectedResponse.getData(), nullValue());

        verify(userRepository, times(1)).findByEmailAddressAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(userRepository);
    }
}