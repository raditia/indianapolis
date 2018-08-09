package com.gdn;

import com.gdn.response.LogisticVendorResponse;

import java.util.ArrayList;
import java.util.List;

public class LogisticVendorResponseUtil {
    public static LogisticVendorResponse logisticVendorResponse = LogisticVendorResponse.builder()
            .id(LogisticVendorUtil.logisticVendorCompleteAttribute.getId())
            .name(LogisticVendorUtil.logisticVendorCompleteAttribute.getName())
            .build();
    public static List<LogisticVendorResponse> logisticVendorResponseList = new ArrayList<LogisticVendorResponse>(){{
        add(logisticVendorResponse);
    }};
}
