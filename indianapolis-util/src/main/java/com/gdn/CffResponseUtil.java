package com.gdn;

import com.gdn.response.CffResponse;

import java.util.ArrayList;
import java.util.List;

public class CffResponseUtil {
    public static CffResponse cffResponseCompleteAttribute = CffResponse.builder()
            .cffId(CffUtil.cffCompleteAttribute.getId())
            .cffGoodList(CffGoodUtil.cffGoodListMinusCff)
            .cbmTotal(CffGoodUtil.cffGoodsCbmTotal)
            .merchantName(CffUtil.cffCompleteAttribute.getMerchant().getName())
            .warehouseName(CffUtil.cffCompleteAttribute.getWarehouse().getAddress())
            .pickupPointAddress(CffUtil.cffCompleteAttribute.getPickupPoint().getPickupAddress())
            .schedulingStatus(CffUtil.cffCompleteAttribute.getSchedulingStatus())
            .build();
    public static List<CffResponse> cffResponseListCompleteAttribute = new ArrayList<CffResponse>(){{
        add(cffResponseCompleteAttribute);
    }};
}
