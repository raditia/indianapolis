package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseQueryResultProcessor implements ItemProcessor<DatabaseQueryResult, List<Pickup>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueryResultProcessor.class);
    private List<DatabaseQueryResult> resultList = new ArrayList<>();

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private FleetService fleetService;

    private int rowCount = 0;
    private List<Pickup> pickupList = new ArrayList<>();

    @Override
    public List<Pickup> process(DatabaseQueryResult databaseQueryResult) throws Exception {
        rowCount = recommendationService.getResultRowCount();
        resultList.add(databaseQueryResult);
        if(allItemsHaveBeenRetrieved()){
            List<Sku> skuList = migrateIntoSkuList(resultList);
            double totalCbm = getTotalCbm(skuList);
            LOGGER.info("TOTAL CBM : " + totalCbm);
            List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityDesc();
            List<BestFitFleet> bestFitFleetList = getBestFitFleetList(fleetOnDbList, totalCbm);
            logBestFleets(bestFitFleetList);

            pickupList = getAssignedFleetsForPickupList(skuList, bestFitFleetList);
            logAssignedFleetsForPickup(pickupList);
            resultList.clear();
        }
        return pickupList;
    }

    private boolean allItemsHaveBeenRetrieved(){
        return resultList.size() == rowCount;
    }

    private List<Sku> migrateIntoSkuList(List<DatabaseQueryResult> resultList){
        List<Sku> skuList = new ArrayList<>();
        List<String> skuIdList = new ArrayList<>();
        String skuId = null;
        for (DatabaseQueryResult result:resultList
             ) {
            List<Vehicle> vehicleList = new ArrayList<>();
            skuId = result.getCffGoods().getId();
            if(!skuIdList.contains(skuId)){
                skuIdList.add(skuId);

                for (DatabaseQueryResult vehicleGetter : resultList
                        ) {
                    if (vehicleGetter.getCffGoods().getId().equals(skuId)) {
                        Vehicle vehicle = Vehicle.builder()
                                .name(vehicleGetter.getAllowedVehicles().getVehicleName())
                                .cbmCapacity(vehicleGetter.getAllowedVehicles().getCbmCapacity())
                                .build();
                        vehicleList.add(vehicle);
                    }
                }

                Sku sku = Sku.builder()
                        .id(result.getCffGoods().getId())
                        .name(result.getCffGoods().getSku())
                        .cbm(result.getCffGoods().getCbm())
                        .quantity(result.getCffGoods().getQuantity())
                        .vehicleList(vehicleList)
                        .build();
                skuList.add(sku);
            }
        }
        return skuList;
    }

    private Double getTotalCbm(List<Sku> skuList){
        Double totalCbm = 0.0;
        for(Sku sku : skuList){
            totalCbm += sku.getCbm() * sku.getQuantity();
        }
        return totalCbm;
    }

    private List<BestFitFleet> getBestFitFleetList(List<Fleet> fleetList, Double totalCbm){
        List<BestFitFleet> maxAmountVehicleList = new ArrayList<>();
        Double maxFleet = 0.0;
        Fleet minFleet = getMinimumBestFitFleetType(fleetList);
        int status = 0;
        while(totalCbm > minFleet.getCbmCapacity()){
            for(Fleet fleet : fleetList){
                if(totalCbm >= fleet.getCbmCapacity()){
                    BestFitFleet maximumFleetAmount = new BestFitFleet();
                    maximumFleetAmount.setFleet(fleet);
                    maxFleet = (totalCbm-(totalCbm % fleet.getCbmCapacity())) / fleet.getCbmCapacity();
                    totalCbm -= maxFleet*fleet.getCbmCapacity();
                    maximumFleetAmount.setAmount(maxFleet.intValue());
                    maxAmountVehicleList.add(maximumFleetAmount);
                }
            }
        }
        //kalau ada sisa dikit banget pasi utus 1 motor(kendaraan cbm terkecil)

        if(totalCbm > 0){
            for(BestFitFleet maxAmount : maxAmountVehicleList){
                if(maxAmount.getFleet().getId().equals(getMinimumBestFitFleetType(fleetList).getId())){
                    maxAmount.setAmount(maxAmount.getAmount()+1);
                    status = 1;
                    break;
                }
            }

            if(status == 0){
                BestFitFleet maxFleetAmount = new BestFitFleet();
                maxFleetAmount.setFleet(getMinimumBestFitFleetType(fleetList));
                maxFleetAmount.setAmount(1);
                maxAmountVehicleList.add(maxFleetAmount);
            }
        }

        return maxAmountVehicleList;
    }

    private Fleet getMinimumBestFitFleetType(List<Fleet> fleetList){
        return fleetList.get(fleetList.size() - 1);
    }

    private void logBestFleets(List<BestFitFleet> bestFitFleetList){
        for (BestFitFleet bestFitFleet : bestFitFleetList){
            System.out.println("Type : " + bestFitFleet.getFleet().getName() + " , " + bestFitFleet.getFleet().getCbmCapacity() + " cbm capacity each");
            System.out.println("Amount : " + bestFitFleet.getAmount());
        }
    }

    public List<Pickup> getAssignedFleetsForPickupList(List<Sku> skuList, List<BestFitFleet> bestFitFleetList){
        List<Pickup> pickupList = new ArrayList<>();
        int i = 0;
        int status = 0;

        for(BestFitFleet bestFitFleet : bestFitFleetList){
            for(i=0; i< bestFitFleet.getAmount(); i++){

                Pickup pickup = new Pickup();
                status = 0;
                pickup.setFleetIdNumber(UUID.randomUUID().toString());
                pickup.setFleet(bestFitFleet.getFleet());
                pickup.setPickupTotalCbm(0.0);
                pickup.setPickupTotalAmount(0);
                List<Detail> detailList = new ArrayList<>();

                while(pickup.getPickupTotalCbm()< bestFitFleet.getFleet().getCbmCapacity() &&
                        !cekSkuListEmpty(skuList)){
                    for(Sku sku : skuList){
                        if(sku.getQuantity() != 0){

                            Detail detail = new Detail();
                            detail.setCbmPickup(0.0);
                            detail.setPickupAmount(0);
                            detail.setSku(sku);

                            while(sku.getQuantity() != 0){
                                if(bestFitFleet.getFleet().getCbmCapacity()-pickup.getPickupTotalCbm()>= sku.getCbm()){

                                    pickup.setPickupTotalCbm(pickup.getPickupTotalCbm() + sku.getCbm());
                                    pickup.setPickupTotalAmount(pickup.getPickupTotalAmount()+ 1);
                                    detail.setCbmPickup(detail.getCbmPickup()+sku.getCbm());
                                    detail.setPickupAmount(detail.getPickupAmount() + 1);
                                    sku.setQuantity(sku.getQuantity()-1);
                                }else{
                                    status = 1;break;
                                }
                            }
                            detailList.add(detail);
                        }
                        if(status == 1){
                            break;
                        }

                    }
                    if(status == 1){
                        break;
                    }
                }
                pickup.setDetailList(detailList);pickupList.add(pickup);
            }
        }

        return pickupList;
    }

    private Boolean cekSkuListEmpty(List<Sku> skuList){
        for(Sku sku : skuList){
            if(sku.getQuantity() != 0)
                return false;
        }
        return true;
    }

    private void logAssignedFleetsForPickup(List<Pickup> pickupList){
        for (Pickup pickup : pickupList){
            System.out.println("################################################");
            System.out.println("Fleet type : " + pickup.getFleet().getName());
            System.out.println("Fleet id number : " + pickup.getFleetIdNumber());
            System.out.println("SKU total picked up : " + pickup.getPickupTotalAmount());
            System.out.println("CBM total picked up : " + pickup.getPickupTotalCbm());
            System.out.println("---------------detail----------------");
            for (Detail detail : pickup.getDetailList()){
                System.out.println("----------------------------------");
                System.out.println("SKU : " + detail.getSku().getName());
                System.out.println("CBM of SKU : " + detail.getCbmPickup());
                System.out.println("Amount of SKU picked up : " + detail.getPickupAmount());
            }
        }
    }

}
