package com.gdn.mapper;

import com.gdn.LoginResponseUtil;
import com.gdn.UserUtil;
import com.gdn.response.LoginResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoginResponseMapper.class)
public class LoginResponseMapperTest {

    @Before
    public void setUp() throws Exception {
        mockStatic(LoginResponseMapper.class);
    }

    @Test
    public void toLoginResponse() {
        given(LoginResponseMapper.toLoginResponse(UserUtil.userCompleteAttribute))
                .willReturn(LoginResponseUtil.loginResponseCompleteAttribute);
        LoginResponse expectedResponse = LoginResponseMapper.toLoginResponse(UserUtil.userCompleteAttribute);
        assertThat(expectedResponse, notNullValue());
        assertThat(expectedResponse, equalTo(LoginResponseUtil.loginResponseCompleteAttribute));
    }
}