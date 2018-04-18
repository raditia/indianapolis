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
            List<Recommendation> rekomendasiList = get3Recommendation();

            for(Recommendation rekomendasi : rekomendasiList){
                System.out.println(rekomendasi.getId()+"///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
                int itung=0;
                for(Pickup pengangkutan : rekomendasi.getPickupList()){
                    System.out.println("Hitungan Ke : "+itung+"-"+pengangkutan.getFleet().getName()+" Id Kendaraan: "+pengangkutan.getFleetIdNumber()+" Jumlah Angkut "+pengangkutan.getPickupTotalAmount());
                    itung+=1;
                }
                System.out.println("CBM Total : "+rekomendasi.getCbmTotal());
                System.out.println("Total SKU : "+rekomendasi.getSkuAmount());
            }
            resultList.clear();
        }
        return pickupList;
    }

    private List<Recommendation> get3Recommendation(){
        List<Recommendation> recommendationList = new ArrayList<>();
        List<Fleet> fleetList = get3MaxFleets();
        Integer recommendationAmount = 3;
        for(Fleet fleet : fleetList){
            if(recommendationAmount <= 0){
                break;
            }
            Recommendation recommendation = getRecommendation(fleet);
            recommendation.setId(UUID.randomUUID().toString());
            recommendationList.add(recommendation);

            recommendationAmount-=1;
        }
        return recommendationList;
    }

    private List<Fleet> get3MaxFleets(){
        List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityDesc();
        List<Fleet> fleetList = new ArrayList<>();
        List<Sku> skuList = migrateIntoSkuList(resultList);
        Fleet maxFleet = getMaxFleet(skuList);
        Integer maxFleetAmount = 3;
        for(Fleet fleet : fleetOnDbList){
            if(maxFleetAmount<=0){
                break;
            }
            if(fleet.getCbmCapacity() <= maxFleet.getCbmCapacity()){
                fleetList.add(fleet);
                maxFleet = fleet;
            }
            maxFleetAmount-=1;
        }
        return fleetList;
    }

    private Fleet getMaxFleet(List<Sku> skuList){
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet tempFleet;
        Double tempCbm;

        for(Fleet fleet : fleetList){
            tempCbm = 0.0;
            for(Sku sku : skuList){
                for(Vehicle vehicle : sku.getVehicleList()){
                    if(vehicle.getCbmCapacity()>=fleet.getCbmCapacity() && fleet.getCbmCapacity()>=sku.getCbm()){
                        tempCbm+=sku.getCbm()*sku.getQuantity();
                        break;
                    }
                }
            }
            if(tempCbm>=fleet.getMinCbm()){
                tempFleet = fleet;
                return tempFleet;
            }
        }
        return null;
    }

    private Recommendation getRecommendation(Fleet maxFleet){
        Recommendation recommendation = new Recommendation();
        List<Pickup> pickupList = new ArrayList<>();
        List<Sku> skuList;
        skuList = migrateIntoSkuList(resultList);
        double cbmTotal = 0.0f;
        int skuAmount = 0;
        while(!cekHabis(skuList)){
            Pickup pickup = getPickup(skuList,maxFleet);
            pickupList.add(pickup);
            cbmTotal=cbmTotal+pickup.getPickupTotalCbm();
            skuAmount+=pickup.getPickupTotalAmount();
        }
        recommendation.setPickupList(pickupList);
        recommendation.setCbmTotal(cbmTotal);
        recommendation.setSkuAmount(skuAmount);
        return recommendation;
    }

    private Pickup getPickup(List<Sku> skuList, Fleet maxFleet) {
        Pickup pickup = new Pickup();
        pickup = initPickup(pickup);
        Fleet fleet = getNextFleet(skuList, maxFleet);

        if(fleet != null){
            pickup.setFleetIdNumber(fleet.getId());
            pickup.setFleet(fleet);
            List<Detail> detailList = getDetailList(skuList,fleet);
            if(detailList != null){
                pickup.setPickupTotalCbm(getCbmPickupTotal(detailList));
                pickup.setPickupTotalAmount(getPickupAmountTotal(detailList));
                pickup.setDetailList(detailList);
            }
        }
        return pickup;
    }

    private double getCbmPickupTotal(List<Detail> detailList) {
        double total = 0.0f;
        for(Detail detail : detailList){
            total=total+detail.getCbmPickup();
        }return total;
    }

    private int getPickupAmountTotal(List<Detail> detailList) {
        int amount=0;
        for(Detail detail : detailList){
            amount+=detail.getPickupAmount();
        }return amount;
    }

    private List<Sku> updateSkuList(List<Sku> skuList, List<Detail> detailList) {
        for(Sku sku: skuList){
            for(Detail detail : detailList){
                if(sku.getId()==detail.getSku().getId() && detail.getPickupAmount()>0){
                    sku.setQuantity(sku.getQuantity()-detail.getPickupAmount());
                }
            }
        }
        return skuList;
    }

    private List<Detail> getDetailList(List<Sku> skuList, Fleet kendaraan) {
        List<Detail> detailList = new ArrayList<>();
        int counter = 0;
        int skuAmount = 0;
        double cbm = 0.0f;
        boolean statusBreak = false;
        for(Sku sku : skuList){
            if(sku.getQuantity() >0){
                skuAmount = 0;
                Detail detail = new Detail();
                detail.setSku(new Sku(sku.getId(), sku.getName(), skuAmount, sku.getCbm(), sku.getVehicleList()));
                counter = sku.getQuantity();
                for(Vehicle vehicle : sku.getVehicleList()){
                    if(vehicle.getCbmCapacity()>=kendaraan.getCbmCapacity()){
                        while(cbm<kendaraan.getCbmCapacity() && counter>0){
                            if((cbm+sku.getCbm()) > kendaraan.getCbmCapacity()){
                                statusBreak = true;
                                break;
                            }else{
                                cbm = cbm+sku.getCbm();
                                skuAmount+=1;
                            }
                            counter--;
                        }
                        break;
                    }
                    if(statusBreak){
                        break;
                    }
                }
                detail.setCbmPickup(skuAmount * sku.getCbm());
                detail.setPickupAmount(skuAmount);
                if(skuAmount >0){
                    detailList.add(detail);
                }
                if(statusBreak){
                    break;
                }
            }
        }
        return detailList;
    }

    private Fleet getNextFleet(List<Sku> skuList, Fleet maxFleet){
        List<Fleet> kendaraanList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet tempFleet = new Fleet();
        Double tempCbm;

        for(Fleet fleet : kendaraanList){
            if(fleet.getCbmCapacity() <= maxFleet.getCbmCapacity()){
                tempCbm = 0.0;
                for(Sku sku : skuList){
                    for(Vehicle kendaraan2 : sku.getVehicleList()){
                        if(kendaraan2.getCbmCapacity()>=fleet.getCbmCapacity()){
                            tempCbm+=sku.getCbm()*sku.getQuantity();
                            break;
                        }
                    }
                }
                if(tempCbm>=fleet.getMinCbm()){
                    tempFleet = fleet;
                    return tempFleet;
                }
            }
        }
        return null;
    }

    private Pickup initPickup(Pickup pickup) {
        pickup.setPickupTotalCbm(0.0f);
        pickup.setFleetIdNumber("0");
        pickup.setPickupTotalAmount(0);
        return pickup;
    }

    private boolean cekHabis(List<Sku> skuList) {
        for(Sku sku : skuList){
            if(sku.getQuantity()>0)
                return false;
        }return true;
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

    private Fleet getMinimumBestFitFleetType(List<Fleet> fleetList){
        return fleetList.get(fleetList.size() - 1);
    }

    private Boolean cekSkuListEmpty(List<Sku> skuList){
        for(Sku sku : skuList){
            if(sku.getQuantity() != 0)
                return false;
        }
        return true;
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
