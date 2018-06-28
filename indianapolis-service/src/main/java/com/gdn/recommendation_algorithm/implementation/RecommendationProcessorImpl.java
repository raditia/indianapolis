package com.gdn.recommendation_algorithm.implementation;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Recommendation;
import com.gdn.recommendation.Sku;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import com.gdn.helper.RecommendationAlgorithmHelper;
import com.gdn.recommendation_algorithm.PickupProcessorService;
import com.gdn.recommendation_algorithm.RecommendationProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationProcessorImpl implements RecommendationProcessorService {

    @Autowired
    private FleetProcessorService fleetProcessorService;
    @Autowired
    private PickupProcessorService pickupProcessorService;

    @Autowired
    private RecommendationAlgorithmHelper recommendationAlgorithmHelper;

    /**
     * Digunakan untuk mendapatkan tiga buah rekomendasi
     * Cara : melakukan 3 x perulangan terhadap 3 maximal kendaraan
     * Input : List semua SKU
     * Output : List rekomendasi
     * @return List<Recommendation>
     */
    @Override
    public List<Recommendation> getThreeRecommendation(List<DatabaseQueryResult> resultList, String warehouseId){
        List<Recommendation> recommendationList = new ArrayList<>();
        List<Sku> skuList = recommendationAlgorithmHelper.migrateIntoSkuList(resultList);
        Integer numberOfRecommendation = 3;

        List<Fleet> fleetList = fleetProcessorService.getMaxThree(skuList);
        for(Fleet fleet : fleetList){
            if(numberOfRecommendation <= 0){
                break;
            }
            skuList = recommendationAlgorithmHelper.migrateIntoSkuList(resultList);

            Recommendation recommendation;
            recommendation = getRecommendation(skuList, fleet);
            recommendation.setId("recommendation_result_" + UUID.randomUUID().toString());
            recommendation.setWarehouseId(warehouseId);
            recommendationList.add(recommendation);
            numberOfRecommendation-=1;
        }

        return recommendationList;
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
     * @param maxFleet kendaraan dengan cbm max yang akan berangkat
     * @return Recommendation
     */
    private Recommendation getRecommendation(List<Sku> skuList, Fleet maxFleet){
        Recommendation recommendation = new Recommendation();
        List<Pickup> pickupList = new ArrayList<>();
        Float cbmTotal = 0.0f;
        Integer numberOfSku = 0;
        Fleet maxFleetTemp = maxFleet;

        while(!recommendationAlgorithmHelper.isEmpty(skuList)){
            Pickup pickup = pickupProcessorService.getPickup(skuList, maxFleet);

            cbmTotal = recommendationAlgorithmHelper.formatNormalFloat(cbmTotal+pickup.getPickupTotalCbm());
            numberOfSku += pickup.getPickupTotalAmount();

            if(pickup.getPickupTotalCbm() > 0) {
                pickupList.add(pickup);
                maxFleet = maxFleetTemp;
            } else {
                maxFleet = fleetProcessorService.getUp(maxFleet);
            }

        }
        recommendation.setPickupList(pickupList);
        recommendation.setCbmTotal(cbmTotal);
        recommendation.setSkuAmount(numberOfSku);
        return recommendation;
    }
}
