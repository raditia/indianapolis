package com.gdn.controller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.LoginRequestUtil;
import com.gdn.LoginResponseUtil;
import com.gdn.UserUtil;
import com.gdn.login.LoginService;
import com.gdn.request.LoginRequest;
import com.gdn.response.WebResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void loginWithEmailAndPassword_OK() throws Exception {
        given(loginService.loginWithEmailAndPassword(LoginRequestUtil.loginRequest))
                .willReturn(WebResponse.OK(LoginResponseUtil.loginResponseCompleteAttribute));
        mockMvc.perform(post("/api/login")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(LoginRequestUtil.loginRequest)))
                .andExpect(jsonPath("$.code", equalTo(200)))
                .andExpect(jsonPath("$.status", equalTo("OK")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.userId", equalTo(UserUtil.userCompleteAttribute.getId())))
                .andExpect(jsonPath("$.data.userName", equalTo(UserUtil.userCompleteAttribute.getName())))
                .andExpect(jsonPath("$.data.userRole", equalTo(UserUtil.userCompleteAttribute.getUserRole().getRole())));
        verify(loginService, times(1)).loginWithEmailAndPassword(LoginRequestUtil.loginRequest);
    }

    @Test
    public void loginWithEmailAndPassword_NOTFOUND() throws Exception {
        LoginRequest notExistsLoginRequest = LoginRequest.builder()
                .email("email not exists")
                .password("password wrong")
                .build();
        given(loginService.loginWithEmailAndPassword(notExistsLoginRequest))
                .willReturn(WebResponse.NOT_FOUND());
        mockMvc.perform(post("/api/login")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(notExistsLoginRequest)))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.status", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", equalTo("Not Found")))
                .andExpect(jsonPath("$.data", nullValue()));
        verify(loginService, times(1)).loginWithEmailAndPassword(notExistsLoginRequest);
    }
}