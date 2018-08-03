package com.gdn;

import com.gdn.recommendation.DatabaseQueryResult;

public class DatabaseQueryResultUtil {
    public static DatabaseQueryResult databaseQueryResultCompleteAttribute = DatabaseQueryResult.builder()
            .cffId(CffUtil.cffCompleteAttribute.getId())
            .merchantId(MerchantUtil.merchantCompleteAttribute.getId())
            .pickupPointId(PickupPointUtil.pickupPointCompleteAttribute.getId())
            .warehouseId(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())
            .cffGoods(DatabaseQueryCffGoodsUtil.databaseQueryCffGoodsCompleteAttribute)
            .allowedVehicles(DatabaseQueryAllowedVehiclesUtil.databaseQueryAllowedVehiclesCompleteAttribute)
            .build();
}
