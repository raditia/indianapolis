package com.gdn.recommendation_algorithm;

import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Product;
import com.gdn.recommendation.Vehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static List<Product> migrateIntoProductList(List<DatabaseQueryResult> resultList){
        List<Product> productList = new ArrayList<>();
        List<String> skuIdList = new ArrayList<>();
        String productId = null;
        for (DatabaseQueryResult result:resultList
                ) {
            List<Vehicle> vehicleList = new ArrayList<>();
            productId = result.getCffGoods().getId();
            if(!skuIdList.contains(productId)){
                skuIdList.add(productId);

                for (DatabaseQueryResult vehicleGetter : resultList
                        ) {
                    if (vehicleGetter.getCffGoods().getId().equals(productId)) {
                        Vehicle vehicle = Vehicle.builder()
                                .name(vehicleGetter.getAllowedVehicles().getVehicleName())
                                .cbmCapacity(vehicleGetter.getAllowedVehicles().getCbmCapacity())
                                .build();
                        vehicleList.add(vehicle);
                    }
                }

                Product product = Product.builder()
                        .id(result.getCffGoods().getId())
                        .name(result.getCffGoods().getSku())
                        .cbm(result.getCffGoods().getCbm())
                        .quantity(result.getCffGoods().getQuantity())
                        .vehicleList(vehicleList)
                        .merchantId(result.getMerchantId())
                        .pickupPointId(result.getPickupPointId())
                        .build();
                productList.add(product);
            }
        }
        return productList;
    }

    public static float formatNormalFloat(float input){
        return new BigDecimal(input).setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static boolean empty(List<Product> productList) {
        for(Product product : productList){
            if(product.getQuantity() > 0){
                return false;
            }
        }return true;
    }

}
