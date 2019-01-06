package com.gdn;

import com.gdn.response.WarehouseCategoryResponse;

import java.util.ArrayList;
import java.util.List;

public class WarehouseCategoryResponseUtil {
    public static WarehouseCategoryResponse warehouseCategoryResponseCompleteAttribute = WarehouseCategoryResponse.builder()
            .warehouseId(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getWarehouse().getId())
            .warehouseAddress(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getWarehouse().getAddress())
            .categoryId(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getCategory().getId())
            .categoryName(WarehouseCategoryUtil.warehouseCategoryCompleteAttribute.getCategory().getName())
            .build();
    public static List<WarehouseCategoryResponse> warehouseCategoryResponseListCompleteAttribute = new ArrayList<WarehouseCategoryResponse>(){{
        add(warehouseCategoryResponseCompleteAttribute);
    }};
}
