package com.gdn.util;

import com.gdn.entity.User;

public class UserUtil {
    public static User userCompleteAttribute = User.builder()
            .id("1")
            .name("user 1 name")
            .emailAddress("user 1 email")
            .password("user 1 password")
            .userRole(UserRoleUtil.userRoleCompleteAttribute)
            .build();
    public static User userUploadCff = User.builder()
            .id("1")
            .build();
}
