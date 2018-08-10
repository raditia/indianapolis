package com.gdn;

import com.gdn.request.LoginRequest;

public class LoginRequestUtil {
    public static LoginRequest loginRequest = LoginRequest.builder()
            .email(UserUtil.userCompleteAttribute.getEmailAddress())
            .password(UserUtil.userCompleteAttribute.getPassword())
            .build();
}
