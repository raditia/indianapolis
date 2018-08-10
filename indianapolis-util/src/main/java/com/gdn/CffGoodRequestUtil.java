package com.gdn;

import com.gdn.request.CffGoodRequest;

import java.util.ArrayList;
import java.util.List;

public class CffGoodRequestUtil {
    public static CffGoodRequest cffGood1RequestCompleteAttribute = CffGoodRequest.builder()
            .sku(CffGoodUtil.cffGoodMinusCff1.getSku())
            .length(CffGoodUtil.cffGoodMinusCff1.getLength())
            .width(CffGoodUtil.cffGoodMinusCff1.getWidth())
            .height(CffGoodUtil.cffGoodMinusCff1.getHeight())
            .weight(CffGoodUtil.cffGoodMinusCff1.getWeight())
            .cbm(CffGoodUtil.cffGoodMinusCff1.getCbm())
            .quantity(CffGoodUtil.cffGoodMinusCff1.getQuantity())
            .build();
    public static CffGoodRequest cffGood2RequestCompleteAttribute = CffGoodRequest.builder()
            .sku(CffGoodUtil.cffGoodMinusCff2.getSku())
            .length(CffGoodUtil.cffGoodMinusCff2.getLength())
            .width(CffGoodUtil.cffGoodMinusCff2.getWidth())
            .height(CffGoodUtil.cffGoodMinusCff2.getHeight())
            .weight(CffGoodUtil.cffGoodMinusCff2.getWeight())
            .cbm(CffGoodUtil.cffGoodMinusCff2.getCbm())
            .quantity(CffGoodUtil.cffGoodMinusCff2.getQuantity())
            .build();
    public static List<CffGoodRequest> cffGoodRequestListCompleteAttribute = new ArrayList<CffGoodRequest>(){{
        add(cffGood1RequestCompleteAttribute);
        add(cffGood2RequestCompleteAttribute);
    }};
}
