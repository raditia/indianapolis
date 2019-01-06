package com.gdn;

import com.gdn.response.LoginResponse;

public class LoginResponseUtil {
    public static LoginResponse loginResponseCompleteAttribute = LoginResponse.builder()
            .userId(UserUtil.userCompleteAttribute.getId())
            .userName(UserUtil.userCompleteAttribute.getName())
            .userRole(UserUtil.userCompleteAttribute.getUserRole().getRole())
            .build();
}
