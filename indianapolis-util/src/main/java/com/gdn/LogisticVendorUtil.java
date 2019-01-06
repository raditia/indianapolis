package com.gdn;

import com.gdn.entity.LogisticVendor;

import java.util.ArrayList;
import java.util.List;

public class LogisticVendorUtil {
    public static LogisticVendor logisticVendorCompleteAttribute = LogisticVendor.builder()
            .id("bes")
            .emailAddress("email bes")
            .name("blibli express service")
            .phoneNumber("telp")
            .build();
    public static List<LogisticVendor> logisticVendorListCompleteAttribute = new ArrayList<LogisticVendor>(){{
        add(logisticVendorCompleteAttribute);
    }};
}
