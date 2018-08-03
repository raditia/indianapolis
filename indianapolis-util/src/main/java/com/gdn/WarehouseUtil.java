package com.gdn;

import com.gdn.entity.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class WarehouseUtil {
    public static Warehouse warehouseMinusWarehouseCategoryList = Warehouse.builder()
            .id("warehouse_cawang")
            .address("cawang")
            .emailAddress("email warehouse cawang")
            .phoneNumber("telp warehouse cawang")
            .build();
    public static List<Warehouse> warehouseListMinusWarehouseCategoryList = new ArrayList<Warehouse>(){{
        add(warehouseMinusWarehouseCategoryList);
    }};
    public static Warehouse warehouseUploadCff = Warehouse.builder()
            .id("warehouse_cawang")
            .build();
}
