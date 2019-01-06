package com.gdn;

import com.gdn.request.AllowedVehicleRequest;

import java.util.ArrayList;
import java.util.List;

public class AllowedVehicleRequestUtil {
    public static AllowedVehicleRequest allowedVehicleRequestCompleteAttribute = AllowedVehicleRequest.builder()
            .vehicleName(AllowedVehicleUtil.allowedVehicleCompleteAttribute.getVehicleName())
            .build();
    public static List<AllowedVehicleRequest> allowedVehicleRequestListCompleteAttribute = new ArrayList<AllowedVehicleRequest>(){{
        add(allowedVehicleRequestCompleteAttribute);
    }};
}
