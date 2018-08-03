package com.gdn;

import com.gdn.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CffUtil {
    public static Cff cffCompleteAttribute = Cff.builder()
            .id("1")
            .uploadedDate(new Date())
            .cffGoodList(CffGoodUtil.cffGoodListMinusCff)
            .merchant(MerchantUtil.merchantCompleteAttribute)
            .warehouse(WarehouseUtil.warehouseMinusWarehouseCategoryList)
            .pickupPoint(PickupPointUtil.pickupPointCompleteAttribute)
            .tp(UserUtil.userCompleteAttribute)
            .schedulingStatus(SchedulingStatus.PENDING)
            .build();
    public static List<Cff> cffListCompleteAttribute = new ArrayList<Cff>(){{
        add(cffCompleteAttribute);
    }};

    public static Cff uploadCffNewMerchantNewPickupPoint = Cff.builder()
            .id("1")
            .tp(UserUtil.userUploadCff)
            .merchant(MerchantUtil.newMerchantUploadCff)
            .warehouse(WarehouseUtil.warehouseUploadCff)
            .cffGoodList(CffGoodUtil.cffGoodListUploadCff)
            .pickupPoint(PickupPointUtil.newPickupPointUploadCff)
            .build();

    public static Cff uploadCffExistingMerchantExistingPickupPoint = Cff.builder()
            .id("1")
            .tp(UserUtil.userUploadCff)
            .merchant(MerchantUtil.existingMerchantUploadCff)
            .warehouse(WarehouseUtil.warehouseUploadCff)
            .cffGoodList(CffGoodUtil.cffGoodListUploadCff)
            .pickupPoint(PickupPointUtil.existingPickupPointUploadCff)
            .build();
}
