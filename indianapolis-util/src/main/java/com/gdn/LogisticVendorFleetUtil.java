package com.gdn;

import com.gdn.entity.LogisticVendorFleet;

import java.util.ArrayList;
import java.util.List;

public class LogisticVendorFleetUtil {
    public static LogisticVendorFleet logisticVendorFleetLogisticVendorOnly = LogisticVendorFleet.builder()
            .logisticVendor(LogisticVendorUtil.logisticVendorCompleteAttribute)
            .build();
    public static List<LogisticVendorFleet> logisticVendorFleetListLogisticVendorOnly = new ArrayList<LogisticVendorFleet>(){{
        add(logisticVendorFleetLogisticVendorOnly);
    }};
}
