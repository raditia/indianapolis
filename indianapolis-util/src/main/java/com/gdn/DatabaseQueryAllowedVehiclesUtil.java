package com.gdn;

import com.gdn.recommendation.DatabaseQueryAllowedVehicles;

public class DatabaseQueryAllowedVehiclesUtil {
    public static DatabaseQueryAllowedVehicles databaseQueryAllowedVehiclesCompleteAttribute = DatabaseQueryAllowedVehicles.builder()
            .vehicleName(FleetUtil.fleetMotorCompleteAttribute.getName())
            .cbmCapacity(FleetUtil.fleetMotorCompleteAttribute.getCbmCapacity())
            .build();
}
