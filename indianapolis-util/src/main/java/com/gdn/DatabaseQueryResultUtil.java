package com.gdn;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.DatabaseQueryResult;

import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryResultUtil {
    public static DatabaseQueryResult databaseQueryResultCompleteAttribute = DatabaseQueryResult.builder()
            .cffId(CffUtil.cffCompleteAttribute.getId())
            .merchantId(MerchantUtil.merchantCompleteAttribute.getId())
            .pickupPointId(PickupPointUtil.pickupPointCompleteAttribute.getId())
            .warehouseId(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())
            .cffGoods(DatabaseQueryCffGoodsUtil.databaseQueryCffGoodsCompleteAttribute)
            .allowedVehicles(DatabaseQueryAllowedVehiclesUtil.databaseQueryAllowedVehiclesCompleteAttribute)
            .build();
    public static List<DatabaseQueryResult> databaseQueryResultList = new ArrayList<DatabaseQueryResult>(){{
        add(databaseQueryResultCompleteAttribute);
    }};
}
