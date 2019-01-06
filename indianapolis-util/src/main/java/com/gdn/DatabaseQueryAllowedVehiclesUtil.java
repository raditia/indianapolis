package com.gdn;

import com.gdn.recommendation.DatabaseQueryAllowedVehicles;

import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryAllowedVehiclesUtil {
    public static DatabaseQueryAllowedVehicles databaseQueryAllowedVehiclesCompleteAttribute = DatabaseQueryAllowedVehicles.builder()
            .vehicleName(FleetUtil.fleetMotorCompleteAttribute.getName())
            .cbmCapacity(FleetUtil.fleetMotorCompleteAttribute.getCbmCapacity())
            .build();
    public static DatabaseQueryAllowedVehicles databaseQueryAllowedVehiclesCompleteAttributeDouble = DatabaseQueryAllowedVehicles.builder()
            .vehicleName(FleetUtil.fleetDouble.getName())
            .cbmCapacity(FleetUtil.fleetDouble.getCbmCapacity())
            .build();
    public static DatabaseQueryAllowedVehicles databaseQueryAllowedVehiclesCompleteAttributeEngkel = DatabaseQueryAllowedVehicles.builder()
            .vehicleName(FleetUtil.fleetEngkel.getName())
            .cbmCapacity(FleetUtil.fleetEngkel.getCbmCapacity())
            .build();
    public static DatabaseQueryAllowedVehicles databaseQueryAllowedVehiclesCompleteAttributeVan = DatabaseQueryAllowedVehicles.builder()
            .vehicleName(FleetUtil.fleetVanCompleteAttribute.getName())
            .cbmCapacity(FleetUtil.fleetVanCompleteAttribute.getCbmCapacity())
            .build();
    public static List<DatabaseQueryAllowedVehicles> databaseQueryAllowedVehiclesCompleteAttributeList = new ArrayList<DatabaseQueryAllowedVehicles>(){{
        add(databaseQueryAllowedVehiclesCompleteAttributeDouble);
        add(databaseQueryAllowedVehiclesCompleteAttributeEngkel);
        add(databaseQueryAllowedVehiclesCompleteAttributeVan);
        add(databaseQueryAllowedVehiclesCompleteAttribute);
    }};
}
