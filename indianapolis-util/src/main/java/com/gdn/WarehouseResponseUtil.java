package com.gdn;

import com.gdn.response.WarehouseResponse;

import java.util.ArrayList;
import java.util.List;

public class WarehouseResponseUtil {
    public static WarehouseResponse warehouseResponseCompleteAttribute = WarehouseResponse.builder()
            .id(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())
            .address(WarehouseUtil.warehouseMinusWarehouseCategoryList.getAddress())
            .build();
    public static List<WarehouseResponse> warehouseResponseListCompleteAttribute = new ArrayList<WarehouseResponse>(){{
        add(warehouseResponseCompleteAttribute);
    }};
}
