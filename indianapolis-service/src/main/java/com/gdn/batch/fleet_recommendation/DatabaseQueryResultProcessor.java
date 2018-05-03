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

public class DatabaseQueryResultProcessor implements ItemProcessor<DatabaseQueryResult, List<Recommendation>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueryResultProcessor.class);
    private List<DatabaseQueryResult> resultList = new ArrayList<>();

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private FleetService fleetService;

    private int rowCount = 0;
    private String warehouseId;

    public DatabaseQueryResultProcessor(String warehouseId) {
        this.warehouseId=warehouseId;
    }

    @Override
    public List<Recommendation> process(DatabaseQueryResult databaseQueryResult) throws Exception {
        rowCount = recommendationService.getResultRowCount(warehouseId);
        resultList.add(databaseQueryResult);
        List<Recommendation> rekomendasiList = new ArrayList<>();
        if(allItemsHaveBeenRetrieved()){
            LOGGER.info("Row count : " + rowCount);
            LOGGER.info("Processing...");
            rekomendasiList = get3Recommendation();
            resultList.clear();
        }
        return rekomendasiList;
    }

    private List<Recommendation> get3Recommendation(){
        List<Recommendation> recommendationList = new ArrayList<>();
        List<Fleet> kendaraanList3 = get3MaxVehicle();
        Integer jumlahRekomendasi = 3;
        for(Fleet kendaraan : kendaraanList3){
            if(jumlahRekomendasi <= 0){
                break;
            }
            Recommendation rekomendasi = new Recommendation();
            rekomendasi = getRecommendation(kendaraan);
            rekomendasi.setId(UUID.randomUUID().toString());
            recommendationList.add(rekomendasi);

            jumlahRekomendasi-=1;
        }
        return recommendationList;
    }

    private List<Fleet> get3MaxVehicle(){
        List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityDesc();
        List<Fleet> vehicleList3 = new ArrayList<>();
        List<Sku> skuList = migrateIntoSkuList(resultList);
        Fleet maxFleet = getMaxKendaraan(skuList);
        Integer jumlahMaxKendaraan = 3;
        for(Fleet kendaraan : fleetOnDbList){
            if(jumlahMaxKendaraan<=0){
                break;
            }
            if(kendaraan.getCbmCapacity() <= maxFleet.getCbmCapacity()){
                vehicleList3.add(kendaraan);
                maxFleet = kendaraan;
            }
            jumlahMaxKendaraan-=1;
        }
        return vehicleList3;
    }

    private Fleet getMaxKendaraan(List<Sku> skuList){
        List<Fleet> kendaraanList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet tempKendaraan = new Fleet();
        Double tempCbm;

        for(Fleet kendaraan : kendaraanList){
            tempCbm = 0.0;
            for(Sku sku : skuList){
                for(Vehicle vehicle : sku.getVehicleList()){
                    if(vehicle.getCbmCapacity()>=kendaraan.getCbmCapacity() && kendaraan.getCbmCapacity()>=sku.getCbm()){
                        tempCbm+=sku.getCbm()*sku.getQuantity();
                        break;
                    }
                }
            }
            if(tempCbm>=kendaraan.getMinCbm()){
                tempKendaraan = kendaraan;
                return tempKendaraan;
            }
        }
        return null;
    }

    private Recommendation getRecommendation(Fleet maxFleet){
        Recommendation rekomendasi = new Recommendation();
        List<Pickup> pengangkutanList = new ArrayList<>();
        List<Sku> skuList = new ArrayList<>();
        skuList = migrateIntoSkuList(resultList);
        double cbmTotal = 0.0f;
        Integer jumlahSku = 0;
        while(!cekHabis(skuList)){
            Pickup pengangkutan = new Pickup();
            pengangkutan = this.getPengangkutan(skuList,maxFleet);
            pengangkutanList.add(pengangkutan);
            cbmTotal=cbmTotal+pengangkutan.getPickupTotalCbm();
            jumlahSku+=pengangkutan.getPickupTotalAmount();
        }
        rekomendasi.setPickupList(pengangkutanList);
        rekomendasi.setCbmTotal(cbmTotal);
        rekomendasi.setSkuAmount(jumlahSku);
        return rekomendasi;
    }

    private Pickup getPengangkutan(List<Sku> skuList, Fleet maxKendaraan) {
        Pickup pengangkutan = new Pickup();
        pengangkutan = initPengangkutan(pengangkutan);
        Fleet kendaraan = getNextKendaraan(skuList, maxKendaraan);

        if(kendaraan != null){
            pengangkutan.setFleetIdNumber(kendaraan.getId());
            pengangkutan.setFleet(kendaraan);
            List<Detail> detailList = new ArrayList<>();
            detailList = getDetailList(skuList,kendaraan);
            if(detailList != null){
                skuList = updateSkuList(skuList, detailList);
                pengangkutan.setPickupTotalCbm(getCbmTotalAngkut(detailList));
                pengangkutan.setPickupTotalAmount(getJumlahTotalPengangkutan(detailList));
                pengangkutan.setDetailList(detailList);
            }
        }
        return pengangkutan;
    }

    private double getCbmTotalAngkut(List<Detail> detailList) {
        double total = 0.0f;
        for(Detail detail : detailList){
            total=total+detail.getCbmPickup();
        }return total;
    }

    private Integer getJumlahTotalPengangkutan(List<Detail> detailList) {
        Integer jumlah=0;
        for(Detail detail : detailList){
            jumlah+=detail.getPickupAmount();
        }return jumlah;
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
        Integer counter = 0;
        Integer jumlahSku = 0;
        double cbm = 0.0f;
        boolean statusBreak = false;
        for(Sku sku : skuList){
            if(sku.getQuantity() >0){
                jumlahSku = 0;
                Detail detail = new Detail();
                detail.setSku(new Sku(
                        sku.getId(),
                        sku.getName(),
                        jumlahSku,
                        sku.getCbm(),
                        sku.getVehicleList(),
                        sku.getWarehouseId(),
                        sku.getMerchantId()));
                counter = sku.getQuantity();
                for(Vehicle kendaraan2 : sku.getVehicleList()){
                    if(kendaraan2.getCbmCapacity()>=kendaraan.getCbmCapacity()){
                        while(cbm<kendaraan.getCbmCapacity() && counter>0){
//                            System.out.println("CBM GAN-----------------------------------------"+this.formatNormalFloat(cbm+sku.getCbm())+ " Banding: "+(cbm+sku.getCbm())+" cbm: "+kendaraan.getCbm());
                            if((cbm+sku.getCbm()) > kendaraan.getCbmCapacity()){
                                statusBreak = true;
                                break;
                            }else{
                                cbm = cbm+sku.getCbm();
                                jumlahSku+=1;
                            }
                            counter--;
                        }
                        break;
                    }
                    if(statusBreak == true){
                        break;
                    }
                }
                detail.setCbmPickup(jumlahSku * sku.getCbm());
                detail.setPickupAmount(jumlahSku);
                if(jumlahSku >0){
                    detailList.add(detail);
                }
                if(statusBreak == true){
                    break;
                }
            }
        }
        return detailList;
    }

    public Fleet getNextKendaraan(List<Sku> skuList, Fleet maxKendaraan){
        List<Fleet> kendaraanList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet tempKendaraan = new Fleet();
        Double tempCbm;

        for(Fleet kendaraan : kendaraanList){
            if(kendaraan.getCbmCapacity() <= maxKendaraan.getCbmCapacity()){
                tempCbm = 0.0;
                for(Sku sku : skuList){
                    for(Vehicle kendaraan2 : sku.getVehicleList()){
                        if(kendaraan2.getCbmCapacity()>=kendaraan.getCbmCapacity()){
                            tempCbm+=sku.getCbm()*sku.getQuantity();
                            break;
                        }
                    }
                }
                if(tempCbm>=kendaraan.getMinCbm()){
                    tempKendaraan = kendaraan;
                    return tempKendaraan;
                }
            }
        }
        return null;
    }

    private Pickup initPengangkutan(Pickup pengangkutan) {
        pengangkutan.setPickupTotalCbm(0.0f);
        pengangkutan.setFleetIdNumber("0");
        pengangkutan.setPickupTotalAmount(0);
        return pengangkutan;
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
                        .warehouseId(result.getWarehouseId())
                        .merchantId(result.getMerchantId())
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
