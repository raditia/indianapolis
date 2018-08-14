package com.gdn;

import com.gdn.entity.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CffUtil {
    public static Cff cffCompleteAttribute = Cff.builder()
            .id("1")
            .uploadedDate(setUploadedDate10AM())
            .cffGoodList(CffGoodUtil.cffGoodListMinusCff)
            .merchant(MerchantUtil.merchantCompleteAttribute)
            .warehouse(WarehouseUtil.warehouseMinusWarehouseCategoryList)
            .pickupPoint(PickupPointUtil.pickupPointCompleteAttribute)
            .tp(UserUtil.userCompleteAttribute)
            .schedulingStatus(SchedulingStatus.PENDING)
            .build();
    public static Cff cffUploadedYesterday = Cff.builder()
            .id("1")
            .uploadedDate(setYesterdayUploadedDate())
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
    public static List<Cff> cffListCompleteAttributeWithYesterdayCff = new ArrayList<Cff>(){{
        add(cffCompleteAttribute);
        add(cffUploadedYesterday);
    }};

    private static Date setYesterdayUploadedDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 12);
        calendar.set(Calendar.MILLISECOND, 21);
        return calendar.getTime();
    }

    private static Date setUploadedDate10AM(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

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
