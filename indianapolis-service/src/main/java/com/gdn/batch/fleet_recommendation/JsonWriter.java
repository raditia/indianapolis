package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JsonWriter implements ItemWriter<DatabaseQueryResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriter.class);

    @Autowired
    private FleetService fleetService;

    @Override
    public void write(List<? extends DatabaseQueryResult> items) throws Exception {

        List<Sku> skuList = new ArrayList<>();
        List<String> skuIdList = new ArrayList<>();
        String skuId = null;

        for (DatabaseQueryResult result:items){
            List<Vehicle> vehicleList = new ArrayList<>();

            skuId = result.getCffGoods().getId();

            if(!skuIdList.contains(skuId)) {
                skuIdList.add(skuId);

                for (DatabaseQueryResult vehicleGetter : items
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

        List<Fleet> fleetList = fleetService.findAll();

        recommendation(skuList, fleetList);

    }

    private void recommendation(List<Sku> skuList,
                                List<Fleet> fleetList){
        double totalCbm = getTotalCbm(skuList);
        System.out.println("Total cbm : " + totalCbm);
        System.out.println("Max fleet : " + getMaxFleetType(totalCbm,fleetList).getName());
        System.out.println("------------------------------");
        for (MinimumFleet minimumFleet : getMinimumFleetAmount(fleetList, totalCbm)){
            System.out.println("Type : " + minimumFleet.getFleet().getName() + " , " + minimumFleet.getFleet().getCbmCapacity() + " cbm capacity each");
            System.out.println("Amount : " + minimumFleet.getAmount());
        }

        for (Pickup pickup : getPickup(skuList, getMinimumFleetAmount(fleetList, totalCbm))){
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

    private Double getTotalCbm(List<Sku> skuList){
        Double totalCbm = 0.0;
        for(Sku sku : skuList){
            totalCbm += sku.getCbm() * sku.getCbm();
        }
        return totalCbm;
    }

    private Fleet getMaxFleetType(Double totalCbm,
                                  List<Fleet> fleetList){
        Fleet tempFleet = new Fleet();

        for(Fleet fleet : fleetList){
            if(totalCbm >= fleet.getCbmCapacity()){
                return fleet;
            }
        }
        return tempFleet;
    }

    private Fleet getMinimumFleetType(List<Fleet> fleetList){
        return fleetList.get(fleetList.size() - 1);
    }

    private List<MinimumFleet> getMinimumFleetAmount(List<Fleet> fleetList, Double totalCbm){
        List<MinimumFleet> maxAmountVehicleList = new ArrayList<>();
        Double maxFleet = 0.0;
        Fleet minFleet = getMinimumFleetType(fleetList);
        int status = 0;
        while(totalCbm > minFleet.getCbmCapacity()){
            for(Fleet fleet : fleetList){
                if(totalCbm >= fleet.getCbmCapacity()){
                    MinimumFleet maximumFleetAmount = new MinimumFleet();
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
            for(MinimumFleet maxAmount : maxAmountVehicleList){
                if(maxAmount.getFleet().getId().equals(getMinimumFleetType(fleetList).getId())){
                    maxAmount.setAmount(maxAmount.getAmount()+1);
                    status = 1;
                    break;
                }
            }

            if(status == 0){
                MinimumFleet maxFleetAmount = new MinimumFleet();
                maxFleetAmount.setFleet(getMinimumFleetType(fleetList));
                maxFleetAmount.setAmount(1);
                maxAmountVehicleList.add(maxFleetAmount);
            }
        }

        return maxAmountVehicleList;
    }

    private Boolean cekSkuListEmpty(List<Sku> skuList){
        for(Sku sku : skuList){
            if(sku.getQuantity() != 0)
                return false;
        }
        return true;
    }

    public List<Pickup> getPickup(List<Sku> skuList, List<MinimumFleet> minimumFleetList){
        List<Pickup> pickupList = new ArrayList<>();
        int i = 0;
        int status = 0;

        for(MinimumFleet minimumFleet : minimumFleetList){
            for(i=0;i<minimumFleet.getAmount();i++){

                Pickup pickup = new Pickup();
                status = 0;
                pickup.setFleetIdNumber(UUID.randomUUID().toString());
                pickup.setFleet(minimumFleet.getFleet());
                pickup.setPickupTotalCbm(0.0);
                pickup.setPickupTotalAmount(0);
                List<Detail> detailList = new ArrayList<>();

                while(pickup.getPickupTotalCbm()< minimumFleet.getFleet().getCbmCapacity() &&
                        !cekSkuListEmpty(skuList)){
                    for(Sku sku : skuList){
                        if(sku.getQuantity() != 0){

                            Detail detail = new Detail();
                            detail.setCbmPickup(0.0);
                            detail.setPickupAmount(0);
                            detail.setSku(sku);

                            while(sku.getQuantity() != 0){
                                if(minimumFleet.getFleet().getCbmCapacity()-pickup.getPickupTotalCbm()>= sku.getCbm()){

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

}
