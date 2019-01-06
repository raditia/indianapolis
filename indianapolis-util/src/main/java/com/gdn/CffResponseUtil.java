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
    public static CffResponse cffResponseAfterUploadCff = CffResponse.builder()
            .cffId(CffRequestUtil.cffRequestCompleteAttributeNewMerchantNewPickupPoint.getId())
            .merchantName(CffRequestUtil.cffRequestCompleteAttributeNewMerchantNewPickupPoint.getMerchant().getName())
            .warehouseName(WarehouseUtil.warehouseMinusWarehouseCategoryList.getAddress())
            .cffGoodList(CffGoodUtil.cffGoodListMinusCff)
            .pickupPointAddress(CffRequestUtil.cffRequestCompleteAttributeNewMerchantNewPickupPoint.getPickupPoint().getPickupAddress())
            .cbmTotal(CffGoodUtil.cffGoodsCbmTotal)
            .schedulingStatus(SchedulingStatus.PENDING)
            .build();
    public static List<CffResponse> cffResponseListCompleteAttribute = new ArrayList<CffResponse>(){{
        add(cffResponseCompleteAttribute);
    }};
    public static String newMerchantId = "new merchant id";
    public static String newPickupPointId = "new pickup point id";
}
