package com.gdn.helper;

import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Sku;
import com.gdn.recommendation.Vehicle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationAlgorithmHelper {

    public List<Sku> migrateIntoSkuList(List<DatabaseQueryResult> resultList){
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

    /**
     * Digunakan untuk melakukan pengecekan terhadap list cffGood.
     * Cara : melakukan pengaksesan terhadap setiap cffGood dan melakukan pengecekan terhadap jumlah atau quantity
     *          cffGood tersebut. Fungsi ini memastika bahwa semua quantity cffGood adalah 0
     * @param skuList
     * @return
     */
    public boolean isEmpty(List<Sku> skuList) {
        for(Sku sku : skuList){
            if(sku.getQuantity() > 0){
                return false;
            }
        }return true;
    }

}
