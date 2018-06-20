package com.gdn.mapper;

import com.gdn.entity.WarehouseCategory;
import com.gdn.response.WarehouseCategoryResponse;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseCategoryMapper {

    public static WarehouseCategoryResponse toWarehouseCategoryResponse(WarehouseCategory warehouseCategory){
        return WarehouseCategoryResponse.builder()
                .warehouseId(warehouseCategory.getWarehouse().getId())
                .warehouseAddress(warehouseCategory.getWarehouse().getAddress())
                .categoryId(warehouseCategory.getCategory().getId())
                .categoryName(warehouseCategory.getCategory().getName())
                .build();
    }

    public static List<WarehouseCategoryResponse> toWarehouseCategoryResponseList(List<WarehouseCategory> warehouseCategoryList){
        return warehouseCategoryList.stream()
                .map(WarehouseCategoryMapper::toWarehouseCategoryResponse)
                .collect(Collectors.toList());
    }

}
