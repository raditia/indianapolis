package com.gdn;

import com.gdn.request.WarehouseRequest;

public class WarehouseRequestUtil {
    public static WarehouseRequest warehouseRequestCompleteAttribute = WarehouseRequest.builder()
            .id(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())
            .build();
}
