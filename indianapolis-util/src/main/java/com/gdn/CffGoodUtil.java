package com.gdn;

import com.gdn.entity.CffGood;

import java.util.ArrayList;
import java.util.List;

public class CffGoodUtil {
    public static CffGood cffGoodMinusCff1 = CffGood.builder()
            .id("1")
            .sku("sku1")
            .cbm(0.2f)
            .quantity(5)
            .length(0.0f)
            .width(0.0f)
            .height(0.0f)
            .weight(0.0f)
            .build();
    public static CffGood cffGoodMinusCff2 = CffGood.builder()
            .id("2")
            .sku("sku2")
            .cbm(0.1f)
            .quantity(10)
            .length(0.0f)
            .width(0.0f)
            .height(0.0f)
            .weight(0.0f)
            .build();
    public static List<CffGood> cffGoodListMinusCff = new ArrayList<CffGood>(){{
        add(cffGoodMinusCff1);
        add(cffGoodMinusCff2);
    }};
    public static Float cffGoodsCbmTotal = (cffGoodMinusCff1.getCbm()*cffGoodMinusCff1.getQuantity()) + (cffGoodMinusCff2.getCbm()*cffGoodMinusCff2.getQuantity());

    public static CffGood cffGoodUploadCff = CffGood.builder()
            .sku("sku1")
            .cbm(0.2f)
            .quantity(5)
            .length(0.0f)
            .width(0.0f)
            .height(0.0f)
            .weight(0.0f)
            .build();
    public static CffGood cffGoodUploadCff2 = CffGood.builder()
            .sku("sku2")
            .cbm(0.1f)
            .quantity(10)
            .length(0.0f)
            .width(0.0f)
            .height(0.0f)
            .weight(0.0f)
            .build();
    public static List<CffGood> cffGoodListUploadCff = new ArrayList<CffGood>(){{
        add(cffGoodUploadCff);
        add(cffGoodUploadCff2);
    }};
}
