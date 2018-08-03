package com.gdn;

import com.gdn.entity.Warehouse;

public class WarehouseUtil {
    public static Warehouse warehouseMinusWarehouseCategoryList = Warehouse.builder()
            .id("warehouse_cawang")
            .address("cawang")
            .emailAddress("email warehouse cawang")
            .phoneNumber("telp warehouse cawang")
            .build();
    public static Warehouse warehouseUploadCff = Warehouse.builder()
            .id("warehouse_cawang")
            .build();
}
