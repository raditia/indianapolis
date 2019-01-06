package com.gdn;

import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductUtil {
    public static Product product = Product.builder()
            .id(CffGoodUtil.cffGoodMinusCff1.getId())
            .name(CffGoodUtil.cffGoodMinusCff1.getSku())
            .cbm(CffGoodUtil.cffGoodMinusCff1.getCbm())
            .quantity(CffGoodUtil.cffGoodMinusCff1.getQuantity())
            .merchantId(MerchantUtil.merchantCompleteAttribute.getId())
            .pickupPointId(PickupPointUtil.pickupPointCompleteAttribute.getId())
            .vehicleList(VehicleUtil.vehicleList)
            .build();
    public static Product product1 = Product.builder()
            .id(CffGoodUtil.cffGoodMinusCff1.getId())
            .name(CffGoodUtil.cffGoodMinusCff1.getSku())
            .cbm(0.6f)
            .quantity(2)
            .merchantId(MerchantUtil.merchantCompleteAttribute.getId())
            .pickupPointId(PickupPointUtil.pickupPointCompleteAttribute.getId())
            .vehicleList(VehicleUtil.vehicleList1)
            .build();
    public static Product product2 = Product.builder()
            .id(CffGoodUtil.cffGoodMinusCff1.getId())
            .name(CffGoodUtil.cffGoodMinusCff1.getSku())
            .cbm(0.1f)
            .quantity(2)
            .merchantId(MerchantUtil.merchantCompleteAttribute.getId())
            .pickupPointId(PickupPointUtil.pickupPointCompleteAttribute.getId())
            .vehicleList(VehicleUtil.vehicleList1)
            .build();
    public static List<Product> productList = new ArrayList<Product> (){{
        add(product);
    }};
    public static List<Product> productList1 = new ArrayList<Product> (){{
        add(product1);
    }};
    public static List<Product> productList2 = new ArrayList<Product> (){{
        add(product2);
    }};
    public static Integer productQuantityCanBePickup = 2;
    public static Integer productQuantityAfterUpdate = product.getQuantity()-productQuantityCanBePickup;

}
