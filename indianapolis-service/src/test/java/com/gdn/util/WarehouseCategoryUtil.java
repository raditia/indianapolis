package com.gdn.util;

import com.gdn.entity.WarehouseCategory;

public class WarehouseCategoryUtil {
    public static WarehouseCategory warehouseCategoryCompleteAttribute = WarehouseCategory.builder()
            .id("warehouse_category_1")
            .warehouse(WarehouseUtil.warehouseMinusWarehouseCategoryList)
            .category(CategoryUtil.categoryMinusWarehouseCategoryList)
            .build();
}
