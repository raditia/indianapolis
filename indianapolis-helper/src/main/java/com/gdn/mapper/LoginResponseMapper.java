package com.gdn.mapper;

import com.gdn.entity.User;
import com.gdn.response.LoginResponse;

public class LoginResponseMapper {
    public static LoginResponse toLoginResponse(User user){
        return LoginResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userRole(user.getUserRole().getRole())
                .build();
    }
}
