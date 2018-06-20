package com.gdn.batch.fleet_recommendation;

import com.gdn.cff.CffService;
import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation.*;
import helper.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

public class DatabaseQueryResultProcessor implements ItemProcessor<DatabaseQueryResult, List<Recommendation>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueryResultProcessor.class);
    private List<DatabaseQueryResult> resultList = new ArrayList<>();
    private List<Sku> skuList = new ArrayList<>();

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private FleetService fleetService;
    @Autowired
    private CffService cffService;

    private int rowCount = 0;
    private String warehouseId;

    public DatabaseQueryResultProcessor(String warehouseId) {
        this.warehouseId=warehouseId;
    }

    @Override
    public List<Recommendation> process(DatabaseQueryResult databaseQueryResult) throws Exception {
        rowCount = recommendationService.getResultRowCount(warehouseId, DateHelper.tomorrow());
        LOGGER.info("Row count : " + rowCount);
        resultList.add(databaseQueryResult);
        List<Recommendation> rekomendasiList = new ArrayList<>();
        if(allItemsHaveBeenRetrieved()){
            LOGGER.info("Processing...");
            this.skuList=migrateIntoSkuList(resultList);
            rekomendasiList = getThreeRecommendation();
            System.out.println(this.getMaxFleet(this.skuList).getName());
            for(Fleet fleet : this.getThreeMaxFleet()){
                System.out.println("00 "+fleet.getName());
            }
            for (DatabaseQueryResult item:resultList
                    ) {
                cffService.updateSchedulingStatus(item.getCffId());
                System.out.println("-------------------------"+item.getCffGoods().getSku());
            }
            resultList.clear();
        }
        return rekomendasiList;
    }

    /**
     * Digunakan untuk mendapatkan tiga buah rekomendasi
     * Dengan cara melakukan 3 x perulangan terhadap 3 maximal kendaraan
     * Input : -
     * Output : List rekomendasi
     * @return
     */
    private List<Recommendation> getThreeRecommendation(){
        List<Recommendation> recommendationList = new ArrayList<>();
        List<Fleet> fleetList = getThreeMaxFleet();
        Integer numberOfRecommendation = 3;

        for(Fleet fleet : fleetList){
            if(numberOfRecommendation <= 0){
                break;
            }
            Recommendation recommendation = new Recommendation();
            this.skuList=migrateIntoSkuList(resultList);
            System.out.println("---masuk kendaraan : "+fleet.getName());
            recommendation = getRecommendation(fleet);
            recommendation.setId("recommendation_result_" + UUID.randomUUID().toString());
            recommendation.setWarehouseId(warehouseId);
            recommendationList.add(recommendation);
            numberOfRecommendation-=1;
        }

        return recommendationList;
    }

    /**
     * Untuk mendapatkan 3 buah kendaraan maksimal yang akan digunakan
     * @return
     */
    private List<Fleet> getThreeMaxFleet(){
        List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityDesc();
        List<Fleet> fleetList = new ArrayList<>();
        List<Sku> skuList = migrateIntoSkuList(resultList);

        Fleet maxFleet = getMaxFleet(skuList);
        Integer numberOfMaxFleet = 3;
        for(Fleet fleet : fleetOnDbList){
            if(numberOfMaxFleet<=0){
                break;
            }
            if(fleet.getCbmCapacity() <= maxFleet.getCbmCapacity()){
                fleetList.add(fleet);
                maxFleet = fleet;
                numberOfMaxFleet-=1;
            }
        }
        return fleetList;
    }

    /**
     * Untuk mendapatkan kendaraan dengan kapasitas cbm paling maksimal yang akan digunakan
     * @param skuList
     * @return
     */
    private Fleet getMaxFleet(List<Sku> skuList){
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet fleetTemp = new Fleet();
        fleetTemp.setCbmCapacity(0.0f);

        for(Fleet fleet : fleetList){
            System.out.println(":: For Fleet : "+fleet.getName()+" With Cbm : "+this.getCbmForFleet(fleet, skuList));
            if(this.getCbmForFleet(fleet, skuList) >= fleet.getMinCbm() && fleet.getCbmCapacity() > fleetTemp.getCbmCapacity()){
                fleetTemp = fleet;
            }
        }
        return fleetTemp;
    }


    private Float getCbmForFleet(Fleet fleet, List<Sku> skuList){
        Float cbm = 0.0f;

        for(Sku sku : skuList){
            for(Vehicle vehicle : sku.getVehicleList()){
                if(fleet.getName().equals(vehicle.getName())){
                    cbm += this.formatNormalFloat(sku.getCbm() * sku.getQuantity());
                }
            }
        }
        return cbm;
    }

    private Recommendation getRecommendation(Fleet maxFleet){
        Recommendation recommendation = new Recommendation();
        List<Pickup> pickupList = new ArrayList<>();
        Float totalCbm = 0.0f;
        Integer numberOfSku = 0;
        Integer counterPengangkutan = 0;
        Fleet maxFleetTemp = maxFleet;
        while(!isEmpty(this.skuList)){
            counterPengangkutan+=1;
            System.out.println(counterPengangkutan+" counterPengangkutan");

            Pickup pickup = new Pickup();
            pickup = this.getPickup(skuList, maxFleet);
            System.out.println(pickup.getFleet().getName()+" nama kendaraan");
            for (Detail detail : pickup.getDetailList()){
                System.out.println(detail.getSku().getName()+" pickup00");
            }
            totalCbm = this.formatNormalFloat(totalCbm+pickup.getPickupTotalCbm());
            numberOfSku += pickup.getPickupTotalAmount();
            if(pickup.getPickupTotalCbm()>0) {
                pickupList.add(pickup);
                maxFleet = maxFleetTemp;
            } else {
                maxFleet = getUpFleet(maxFleet);
            }

            System.out.println(numberOfSku+" numberOfSku");
            System.out.println("Cbm Angkut : "+pickup.getPickupTotalCbm());
            System.out.println(totalCbm+" totalCbm-------------------");
        }
        recommendation.setPickupList(pickupList);
        recommendation.setCbmTotal(totalCbm);
        recommendation.setSkuAmount(numberOfSku);
        return recommendation;
    }

    private Fleet getUpFleet(Fleet maxFleet) {
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityAsc();

        for (Fleet fleet : fleetList) {
            if (fleet.getCbmCapacity() > maxFleet.getCbmCapacity()){
                return fleet;
            }
        }
        return null;
    }

    private boolean isEmpty(List<Sku> skuList) {
        for(Sku sku : skuList){
            if(sku.getQuantity()>0)
                return false;
        }return true;
    }

    private Pickup getPickup(List<Sku> skuList, Fleet maxKendaraan) {
        Pickup pengangkutan = new Pickup();
        pengangkutan = initPengangkutan(pengangkutan);
        Fleet kendaraan = getNextKendaraan(skuList, maxKendaraan);

        if(kendaraan != null){
            pengangkutan.setFleetIdNumber(kendaraan.getId());
            pengangkutan.setFleet(kendaraan);
            List<Detail> detailList = new ArrayList<>();
            detailList = getDetailList(skuList,kendaraan);
            if(detailList != null){
                this.skuList = updateSkuList(skuList, detailList);
                pengangkutan.setPickupTotalCbm(getCbmTotalAngkut(detailList));
                pengangkutan.setPickupTotalAmount(getJumlahTotalPengangkutan(detailList));
                pengangkutan.setDetailList(detailList);
            }
        }
        return pengangkutan;
    }

    private Float getCbmTotalAngkut(List<Detail> detailList) {
        Float total = 0.0f;
        for(Detail detail : detailList){
            total = formatNormalFloat(total+detail.getCbmPickup());
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
        Float cbm = 0.0f;
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
                        sku.getMerchantId(),
                        sku.getPickupPointId()));
                counter = sku.getQuantity();
                for(Vehicle kendaraan2 : sku.getVehicleList()){
                    if(kendaraan2.getCbmCapacity()>=kendaraan.getCbmCapacity()){
                        while(cbm<kendaraan.getCbmCapacity() && counter>0){
//                            System.out.println("CBM GAN-----------------------------------------"+this.formatNormalFloat(cbm+sku.getCbm())+ " Banding: "+(cbm+sku.getCbm())+" cbm: "+kendaraan.getCbm());
                            if(this.formatNormalFloat(cbm+sku.getCbm()) > kendaraan.getCbmCapacity()){
                                statusBreak = true;
                                break;
                            }else{
                                cbm = this.formatNormalFloat(cbm+sku.getCbm());
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
                detail.setCbmPickup(this.formatNormalFloat(jumlahSku * sku.getCbm()));
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
                            tempCbm += formatNormalFloat(sku.getCbm()*sku.getQuantity());
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
                        .merchantId(result.getMerchantId())
                        .pickupPointId(result.getPickupPointId())
                        .build();
                skuList.add(sku);
            }
        }
        return skuList;
    }

    public float formatNormalFloat(float input){
        return new BigDecimal(input).setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
