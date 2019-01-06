package com.gdn;

import com.gdn.entity.WarehouseCategory;

import java.util.ArrayList;
import java.util.List;

public class WarehouseCategoryUtil {
    public static WarehouseCategory warehouseCategoryCompleteAttribute = WarehouseCategory.builder()
            .id("warehouse_category_1")
            .warehouse(WarehouseUtil.warehouseMinusWarehouseCategoryList)
            .category(CategoryUtil.categoryMinusWarehouseCategoryList)
            .build();
    public static List<WarehouseCategory> warehouseCategoryListCompleteAttribute = new ArrayList<WarehouseCategory>(){{
        add(warehouseCategoryCompleteAttribute);
    }};
}
