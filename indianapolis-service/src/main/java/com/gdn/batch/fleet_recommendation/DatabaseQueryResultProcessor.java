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
        List<Recommendation> recommendationList = new ArrayList<>();
        if(allItemsHaveBeenRetrieved()){
            LOGGER.info("Processing...");
            this.skuList=migrateIntoSkuList(resultList);
            recommendationList = getThreeRecommendation();
            for (DatabaseQueryResult item : resultList) {
                cffService.updateSchedulingStatus(item.getCffId());
            }
            resultList.clear();
        }
        return recommendationList;
    }

    /**
     * Digunakan untuk mendapatkan tiga buah rekomendasi
     * Cara : melakukan 3 x perulangan terhadap 3 maximal kendaraan
     * Input : List semua SKU
     * Output : List rekomendasi
     * @return List<Recommendation>
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
            this.skuList = migrateIntoSkuList(resultList);
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
     * Cara : Melakukan perulangan sebanyak kendaraan dalam db,
     *          jika cbm kendaraan tersebut lebih besar dari cbm maxKendaraan
     *          maka akan ditampung dalam list
     *        Perulangan dilakukan sampai didapatkan 3 buah kendaraan atau
     *          sampai data kendaraan pada db habis
     * Input : List master kendaraan dari basis data secara descending dan kendaraan maximal
     * Output : List 3 buah kendaraan maksimal yang akan digunakan
     * @return List<Fleet>
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
     * Cara : Seperti melakukan pencarian bilangan terbesar. Dimana digunakan variabel penampung, dan membandingkan
     *          cbm angkut (yang dapat diangkut) dengan cbm pada variabel penampung.
     *        Fungsi akan mereturnkan kendaraan dengan cbm terbesar, dengan cbm angkut yang melebihi min cbm kendaraan tersebut
     * Input : List master kendaraan dari basis data secara descending, list sku, cbm angkut dari masing" kendaraan
     * @return Fleet
     */
    private Fleet getMaxFleet(List<Sku> skuList){
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet fleetTemp = new Fleet();
        fleetTemp.setCbmCapacity(0.0f);

        for(Fleet fleet : fleetList){
            if(this.getCbmForFleet(fleet, skuList) >= fleet.getMinCbm() && fleet.getCbmCapacity() > fleetTemp.getCbmCapacity()){
                fleetTemp = fleet;
            }
        }
        return fleetTemp;
    }

    /**
     * Untuk mengetahui cbm angkut dari jenis kendaraan tertentu
     * Cara : Dengan melakukan perulangan atau pengaksesan terhadap semua sku,
     *          kemudian mengakses semua kendaraan yang available untuk sku tersebut,
     *          jika nama dari kendaraan pada sku tersebut dengan nama dari kendaraan yang diperiksa (param input) sama,
     *          maka cbm sku tersebut di kali dengan quantitynya kemudain di tampung pada variabel cbm.
     *        Ketika perulangan selesai, maka pada variabel cbm telah tertampung total cbm dari seluruh sku yang dapat diangkut oleh kendaraan jenis tertentu
     * @param fleet dan skuList
     * @return cbm angkut ( yang dapat diangkut) untuk jenis kendaraan tertentu
     */
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

    /**
     * Digunakan untuk mendapatkan 1 set rekomendasi pengangkutan (keseluruhan diperlukan 3 set rekomendasi)
     * Cara : Fungsi ini akan melakukan perulangan selama data list sku belum habis untuk diproses.
     *          Setiap 1 kali perulangan akan diperoleh satu pengangkutan dengan menggunakan method getPickup
     *          Selain itu akan dihitung cbmTotal yang sudah masuk dalam rekomendasi, dan jumlah SKU nya.
     *          Pada saat perulangan, ada kalanya ditemukan kondisi dimana sebuah sku memiliki cbm yang lebih besar dari kapasitas cbm maxKendaraan,
     *          yang dapat menyebabkan infinity loop. Hal ini dapat diatasi dengan mengganti maxFleet dengan kendaraan dengan kapasitas diatasnya.
     *          Setelah hal tersebut teratasi, maxFleet akan dikembalikan pada kendaraan semula.
     *          Sebuah rekomendasi memiliki beberapa pengangkutan (berbeda" kendaraan), totalCbm, dan jumlah sku.
     * @param maxFleet
     * @return Recommendation
     */
    private Recommendation getRecommendation(Fleet maxFleet){
        Recommendation recommendation = new Recommendation();
        List<Pickup> pickupList = new ArrayList<>();
        Float cbmTotal = 0.0f;
        Integer numberOfSku = 0;
        Fleet maxFleetTemp = maxFleet;

        while(!isEmpty(this.skuList)){
            Pickup pickup = new Pickup();
            pickup = this.getPickup(skuList, maxFleet);

            cbmTotal = this.formatNormalFloat(cbmTotal+pickup.getPickupTotalCbm());
            numberOfSku += pickup.getPickupTotalAmount();

            if(pickup.getPickupTotalCbm() > 0) {
                pickupList.add(pickup);
                maxFleet = maxFleetTemp;
            } else {
                maxFleet = getUpFleet(maxFleet);
            }

        }
        recommendation.setPickupList(pickupList);
        recommendation.setCbmTotal(cbmTotal);
        recommendation.setSkuAmount(numberOfSku);
        return recommendation;
    }

    /**
     * Digunakan untuk mendapatkan kendaraan dengan kapasitas cbm satu tingkat diatas cbm kendaraan parameter input.
     * Cara : Melakukan pengaksesan pada setiap data kendaraan db dan membandingkan cbm kendaraan tersebut dengan cbm parameter input.
     *          Fungsi akan me-return kendaraan yang memiliki cbm diatas cbm parameter input
     * Input : List kendaraan dalam db secara Asc berdasarkan cbm nya
     * @param maxFleet
     * @return Fleet
     */
    private Fleet getUpFleet(Fleet maxFleet) {
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityAsc();

        for (Fleet fleet : fleetList) {
            if (fleet.getCbmCapacity() > maxFleet.getCbmCapacity()){
                return fleet;
            }
        }
        return null;
    }

    /**
     * Digunakan untuk melakukan pengecekan terhadap list sku.
     * Cara : melakukan pengaksesan terhadap setiap sku dan melakukan pengecekan terhadap jumlah atau quantity
     *          sku tersebut. Fungsi ini memastika bahwa semua quantity sku adalah 0
     * @param skuList
     * @return
     */
    private boolean isEmpty(List<Sku> skuList) {
        for(Sku sku : skuList){
            if(sku.getQuantity() > 0){
                return false;
            }
        }return true;
    }

    /**
     * Digunakan untuk mendapatkan sebuah pengangkutan berdasarkan sku yang ada dan maxFleet
     * Cara : Pertama akan ditentukan kendaraan apa yang tepat untuk mengangkut sku yang ada, menggunakan fungsi getNextFleet
     *          Setelah itu akan dilakukan proses untuk mendapatkan detail sku yang diangkut pada pengangkutan ini
     *          dengan menggunakan fungsi getDetailList.
     *          Data sku pada lsit sku akan diperbarui untuk proses pengangkutan selanjutnya.
     *        Setaip pengangkutan memiliki atribut berupa kendaraan, detail sku yang diangkut, total cbm, dan jumlah total sku yang diangkut.
     * @param skuList
     * @param maxFleet
     * @return
     */
    private Pickup getPickup(List<Sku> skuList, Fleet maxFleet) {
        Pickup pickup = new Pickup();
        pickup = initPickup(pickup);
        Fleet fleet = getNextFleet(skuList, maxFleet);

        if(fleet != null){
            pickup.setFleetIdNumber(fleet.getId());
            pickup.setFleet(fleet);
            List<Detail> detailList = new ArrayList<>();
            detailList = getDetailList(skuList,fleet);
            if(detailList != null){
                this.skuList = updateSkuList(skuList, detailList);
                pickup.setPickupTotalCbm(getCbmTotalPickup(detailList));
                pickup.setPickupTotalAmount(getJumlahTotalPengangkutan(detailList));
                pickup.setDetailList(detailList);
            }
        }
        return pickup;
    }

    /**
     * Digunakan untuk melakukan perhitungan terhadap total cbm yang diangkut oleh sebuah pengangkutan.
     * Cara : dilakukan pengaksesan terhadap setiap detail sku, kemudian dilakukan penghitungan dengan mengalikan cbm sku tersebut dengan jumlah yang diangkut.
     * @param detailList
     * @return total cbm untuk pengangkutan tertentu
     */
    private Float getCbmTotalPickup(List<Detail> detailList) {
        Float total = 0.0f;

        for(Detail detail : detailList){
            total = formatNormalFloat(total+detail.getCbmPickup());
        }
        return total;
    }

    /**
     * Digunakan untuk melakukan penghitungan terhadap jumlah sku yang diangkut oleh sebuah pengangkutan
     * Cara : dilakukan pengaksesan terhadap setiap detai sku, kemudian menghitung keseluruhan jumlah sku
     * @param detailList
     * @return totalPengangkutan
     */
    private Integer getJumlahTotalPengangkutan(List<Detail> detailList) {
        Integer jumlah=0;

        for(Detail detail : detailList){
            jumlah+=detail.getPickupAmount();
        }
        return jumlah;
    }

    /**
     * Digunakan untuk memperbaui data pada sku list. Dengan ini apabila setelah dilakukan pengangkutan (pick up) maka sku list diupdate.
     * Sku yang telah ada di pengangkutan akan dihapus dari sku list.
     * Cara : dilakukan pengaksesan terhadap seluruh sku list. Jika ditemukan sku yang ada pada detailSku (sudah di pick up) maka akan dikurangi jumlahnya sebanyak yang di pickup.
     * @param skuList
     * @param detailList
     * @return List<Sku>
     */
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

    /**
     * Digunakan untuk mendapatkan detail sku yang akan diangkut oleh sebuah pengangkutan.
     * Cara : Dilakukan pengaksesan terhadap sku pada skuList yang masih belum mendapatkan pengangkutan (quantity > 0 ).
     *          Kemudian dilakukan pengaksesan terhadap setiap kendaraan available pada sku tersebut. Jika ditemukan kendaraan dengan cbm >= cbm kendaraan parameter input,
     *          maka akan dilakukan pengecekan apakah space cbm pada kendaraan tersebut( var cbm ) masih bisa menampung sebuah sku. Jika bisa maka sebuah sku akan dimasukkan dalam pengangkutan tersebut.
     * @param skuList
     * @param fleet
     * @return
     */
    private List<Detail> getDetailList(List<Sku> skuList, Fleet fleet) {
        List<Detail> detailList = new ArrayList<>();
        Integer counter = 0;
        Integer skuAmount = 0;
        Float cbm = 0.0f;
        boolean statusBreak = false;
        for(Sku sku : skuList){
            if(sku.getQuantity() >0){
                skuAmount = 0;
                Detail detail = new Detail();
                detail.setSku(new Sku(
                        sku.getId(),
                        sku.getName(),
                        skuAmount,
                        sku.getCbm(),
                        sku.getVehicleList(),
                        sku.getMerchantId(),
                        sku.getPickupPointId()));
                counter = sku.getQuantity();
                for(Vehicle vehicle : sku.getVehicleList()){
                    if(vehicle.getCbmCapacity()>=fleet.getCbmCapacity()){
                        while(cbm<fleet.getCbmCapacity() && counter>0){
                            if(this.formatNormalFloat(cbm+sku.getCbm()) > fleet.getCbmCapacity()){
                                statusBreak = true;
                                break;
                            }else{
                                cbm = this.formatNormalFloat(cbm+sku.getCbm());
                                skuAmount+=1;
                            }
                            counter--;
                        }
                        break;
                    }
                    if(statusBreak == true){
                        break;
                    }
                }
                detail.setCbmPickup(this.formatNormalFloat(skuAmount * sku.getCbm()));
                detail.setPickupAmount(skuAmount);
                if(skuAmount >0){
                    detailList.add(detail);
                }
                if(statusBreak == true){
                    break;
                }
            }
        }
        return detailList;
    }

    /**
     * Digunakan untuk mendapatkan jenis kendaraan untuk melanjutkan proses rekomendasi
     * Cara : Melakukan pengaksesan terhadap masing-masing kendaraan dalam basis data,
     *          jika ditemukan kendaraan dengan cbm <= cbm maxFleet maka akan dilakukan pengaksesan terhadap setiap sku.
     *          Untuk masing-masing kendaraan available pada setiap sku, jika cbm kendaraan available >= kendaraan (dari db)
     *          maka cbm total dari sku tersebut (cbm * quantity) akan ditampung.
     *          Dengan demikian dapat diketahui berapa total cbm yang dapat diangkut oleh jenis kendaraan tertentu(parameter).
     *          Jika cbm tersebut melebihi cbm minimal dari kendaraan, maka pengangkutan selanjutnya menggunakan kendaraan tersebut.
     *          Jika tidak maka proses berlanjut ke kendaraan dengan cbm dibawahnya.
     *
     * @param skuList
     * @param maxFleet
     * @return
     */
    public Fleet getNextFleet(List<Sku> skuList, Fleet maxFleet){
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet fleetTemp = new Fleet();
        Double tempCbm;

        for(Fleet fleet : fleetList){
            if(fleet.getCbmCapacity() <= maxFleet.getCbmCapacity()){
                tempCbm = 0.0;
                for(Sku sku : skuList){
                    for(Vehicle vehicle : sku.getVehicleList()){
                        if(vehicle.getCbmCapacity()>=fleet.getCbmCapacity()){
                            tempCbm += formatNormalFloat(sku.getCbm()*sku.getQuantity());
                            break;
                        }
                    }
                }
                if(tempCbm>=fleet.getMinCbm()){
                    fleetTemp = fleet;
                    return fleetTemp;
                }
            }
        }
        return null;
    }

    /**
     * Digunakan untuk menginisialisasi nilai awal dari sebuah pengangkutan
     * @param pickup
     * @return
     */
    private Pickup initPickup(Pickup pickup) {
        pickup.setPickupTotalCbm(0.0f);
        pickup.setFleetIdNumber("0");
        pickup.setPickupTotalAmount(0);
        return pickup;
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

    /**
     * Digunakan untuk menghasilkan penormalan pada hasil operasi pada tipe data float.
     *      Berdasarkan pengamatan dan percobaan, operasi penjumlahan dengan tipe data Float menyebabkan galat, meskipun dengan nilai yang kecil, namun sangat berpengaruh pada proses rekomendasi ini,
     *      yang biasanya menyebabkan jumlah cbm yang tidak sesuai setelah melakukan banyak proses operasi aritmatika
     * @param input
     * @return
     */
    public float formatNormalFloat(float input){
        return new BigDecimal(input).setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
