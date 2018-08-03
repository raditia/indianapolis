package com.gdn.util;

import com.gdn.recommendation.DatabaseQueryCffGoods;

public class DatabaseQueryCffGoodsUtil {
    public static DatabaseQueryCffGoods databaseQueryCffGoodsCompleteAttribute = DatabaseQueryCffGoods.builder()
            .id(CffGoodUtil.cffGoodMinusCff1.getId())
            .cbm(CffGoodUtil.cffGoodMinusCff1.getCbm())
            .quantity(CffGoodUtil.cffGoodMinusCff1.getQuantity())
            .sku(CffGoodUtil.cffGoodMinusCff1.getSku())
            .build();
}
