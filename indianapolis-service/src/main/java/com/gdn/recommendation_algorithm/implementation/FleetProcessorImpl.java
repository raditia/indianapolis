package com.gdn.recommendation_algorithm.implementation;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Product;
import com.gdn.recommendation.Vehicle;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import com.gdn.recommendation_algorithm.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FleetProcessorImpl implements FleetProcessorService {


    @Autowired
    private FleetService fleetService;

    @Override
    public List<Fleet> getTopThreeFleetsWillUsed(List<DatabaseQueryResult> productResultList) {
        List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityDesc();
        List<Fleet> topThreeFleetsWillUsed = new ArrayList<>();

        List<Product> productList = Helper.migrateIntoProductList(productResultList);
        Fleet fleetWithMaxCbmWillUsed = getFleetWithMaxCbmWillUsed(productList);
        Integer fleetCounter = 3;
        for(Fleet fleetOnDb : fleetOnDbList){
            if(fleetCounter<=0){
                break;
            }
            if(fleetOnDb.getCbmCapacity() <= fleetWithMaxCbmWillUsed.getCbmCapacity()){
                topThreeFleetsWillUsed.add(fleetOnDb);
                fleetWithMaxCbmWillUsed = fleetOnDb;
                fleetCounter-=1;
            }
        }
        return topThreeFleetsWillUsed;
    }

    @Override
    public Fleet getNextFleetWillUsed(List<Product> productList, Fleet topFleetWillUsed){
        List<Fleet> fleetOnDBList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet nextFleetWillUsed = new Fleet();
        Float cbmCanBePickupByFleetName = 0.0f;

        boolean getNextFleet = false;

        for(Fleet fleetOnDB : fleetOnDBList){
            if(fleetOnDB.getCbmCapacity() <= topFleetWillUsed.getCbmCapacity()){
                cbmCanBePickupByFleetName = getCbmCanBePickupByFleetName(fleetOnDB, productList);
                if( cbmCanBePickupByFleetName > 0 && cbmCanBePickupByFleetName >= fleetOnDB.getMinCbm()){
                    nextFleetWillUsed = fleetOnDB;
                    getNextFleet = true;
                    break;
                }
            }
        }
        if (!getNextFleet){
            nextFleetWillUsed = getFleetWithMoreCbmCapacity(topFleetWillUsed);
        }

        return nextFleetWillUsed;
    }

    @Override
    public Fleet getFleetWithMoreCbmCapacity(Fleet topFleetWillUsed) {
        Fleet fleetWithMoreCbmCapacity = new Fleet();
        List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityAsc();

        for (Fleet fleetOnDB : fleetOnDbList) {
            if (fleetOnDB.getCbmCapacity() > topFleetWillUsed.getCbmCapacity()){
                fleetWithMoreCbmCapacity = fleetOnDB;
                break;
            }
        }
        return fleetWithMoreCbmCapacity;
    }

    private Fleet getFleetWithMaxCbmWillUsed(List<Product> productList){
        List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet fleetWithMaxCbmWillUsed = new Fleet();

        for(Fleet fleetOnDB : fleetOnDbList){
            if(this.getCbmCanBePickupByFleetName(fleetOnDB, productList) >= fleetOnDB.getMinCbm()){
                fleetWithMaxCbmWillUsed = fleetOnDB;
                break;
            }
        }
        return fleetWithMaxCbmWillUsed;
    }

    private Float getCbmCanBePickupByFleetName(Fleet fleet, List<Product> productList){
        Float cbmCanBePickup = 0.0f;

        for(Product product : productList){
            for(Vehicle allowedVehicle : product.getVehicleList()){
                if(fleet.getName().equals(allowedVehicle.getName()) && product.getCbm() <= fleet.getCbmCapacity()){
                    cbmCanBePickup += Helper.formatNormalFloat(product.getCbm() * product.getQuantity());
                }
            }
        }
        return cbmCanBePickup;
    }

}
