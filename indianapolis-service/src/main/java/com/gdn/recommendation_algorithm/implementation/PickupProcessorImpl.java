package com.gdn.recommendation_algorithm.implementation;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.DetailPickup;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Product;
import com.gdn.recommendation.Vehicle;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import com.gdn.recommendation_algorithm.Helper;
import com.gdn.recommendation_algorithm.PickupProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class PickupProcessorImpl implements PickupProcessorService {

    @Autowired
    private FleetProcessorService fleetProcessorService;

    @Override
    public Pickup getNextPickup(List<Product> productList, Fleet topFleetWillUsed) {
        Fleet fleetWillUsed = fleetProcessorService.getNextFleetWillUsed(productList, topFleetWillUsed);
        List<DetailPickup> detailPickupList = getDetailPickupList(productList,fleetWillUsed);

        return Pickup.builder()
                .fleetIdNumber(fleetWillUsed.getId())
                .fleet(fleetWillUsed)
                .pickupTotalCbm(getPickupTotalCbm(detailPickupList))
                .pickupTotalAmount(getPickupTotalAmount(detailPickupList))
                .detailPickupList(detailPickupList)
                .build();
    }

    private List<DetailPickup> getDetailPickupList(List<Product> productList, Fleet fleetWillUsed) {
        List<DetailPickup> detailPickupList = new ArrayList<>();
        Integer productQuantityCanBePickup;
        Float restCbmCapacityOnFleet = fleetWillUsed.getCbmCapacity();
        for(Product product : productList){
            if (product.getQuantity() > 0) {
                productQuantityCanBePickup = getProductQuantityCanBePickup(product, fleetWillUsed, restCbmCapacityOnFleet);
                if (productQuantityCanBePickup > 0) {
                    DetailPickup detailPickup = DetailPickup.builder()
                            .product(product)
                            .pickupAmount(productQuantityCanBePickup)
                            .pickupCbm(productQuantityCanBePickup * product.getCbm())
                            .build();
                    detailPickupList.add(detailPickup);
                    updateProductList(productList, product, productQuantityCanBePickup);
                    restCbmCapacityOnFleet = updateRestCbmCapacity(restCbmCapacityOnFleet, productQuantityCanBePickup, product.getCbm());
                }
            }
        }
        return detailPickupList;
    }

    private Float updateRestCbmCapacity(Float restCbmCapacityOnFleet, Integer productQuantityCanBePickup, Float productCbm) {
        return restCbmCapacityOnFleet - (productQuantityCanBePickup * productCbm);
    }

    private void updateProductList(List<Product> productList, Product product, Integer productQuantityCanBePickup) {
        for (Product productOnList : productList){
            if(productOnList.getId().equals(product.getId()) && product.getQuantity()>0){
                productOnList.setQuantity(productOnList.getQuantity() - productQuantityCanBePickup);
            }
        }
    }

    private Integer getProductQuantityCanBePickup(Product product, Fleet fleetWillUsed, Float restCbmCapacityOnFleet){
        Integer productQuantityCanBePickup = 0;
        double restCbmDividedProductCbm = restCbmCapacityOnFleet / product.getCbm();
        for(Vehicle allowedVehicle : product.getVehicleList()){
            if(allowedVehicle.getCbmCapacity() >= fleetWillUsed.getCbmCapacity()){
                productQuantityCanBePickup =  (int) restCbmDividedProductCbm;
                break;
            }
        }
        if (productQuantityCanBePickup >= product.getQuantity()) {
            return product.getQuantity();
        }else {
            return productQuantityCanBePickup;
        }
    }

    private Float getPickupTotalCbm(List<DetailPickup> detailPickupList) {
        Float total = 0.0f;
        for(DetailPickup detailPickup : detailPickupList){
            total = Helper.formatNormalFloat(total+ detailPickup.getPickupCbm());
        }
        return total;
    }

    private Integer getPickupTotalAmount(List<DetailPickup> detailPickupList) {
        Integer counter = 0;
        for(DetailPickup detailPickup : detailPickupList){
            counter+= detailPickup.getPickupAmount();
        }
        return counter;
    }

}
