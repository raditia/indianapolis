package com.gdn.recommendation_algorithm.implementation;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.Detail;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Sku;
import com.gdn.recommendation.Vehicle;
import com.gdn.recommendation_algorithm.FleetProcessorService;
import com.gdn.helper.RecommendationAlgorithmHelper;
import com.gdn.recommendation_algorithm.PickupProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PickupProcessorImpl implements PickupProcessorService {

    @Autowired
    private FleetProcessorService fleetProcessorService;

    @Autowired
    private RecommendationAlgorithmHelper recommendationAlgorithmHelper;

    /**
     * Digunakan untuk mendapatkan sebuah pengangkutan berdasarkan sku yang ada dan maxFleet
     * Cara : Pertama akan ditentukan kendaraan apa yang tepat untuk mengangkut sku yang ada, menggunakan fungsi getNextFleet
     *          Setelah itu akan dilakukan proses untuk mendapatkan detail sku yang diangkut pada pengangkutan ini
     *          dengan menggunakan fungsi getDetailList.
     *          Data sku pada lsit sku akan diperbarui untuk proses pengangkutan selanjutnya.
     *        Setaip pengangkutan memiliki atribut berupa kendaraan, detail sku yang diangkut, total cbm, dan jumlah total sku yang diangkut.
     * @param skuList list Sku yang akan di proses
     * @param maxFleet kendaraan dengan cbm max yang akan berangkat
     * @return PickupFleet
     */
    @Override
    public Pickup getPickup(List<Sku> skuList, Fleet maxFleet) {
        Pickup pickup = new Pickup();
        pickup = initPickup(pickup);
        Fleet fleet = fleetProcessorService.getNext(skuList, maxFleet);

        if(fleet != null){
            pickup.setFleetIdNumber(fleet.getId());
            pickup.setFleet(fleet);
            List<Detail> detailList = getDetailList(skuList,fleet);
            if(detailList != null){
                updateSkuList(skuList, detailList);
                pickup.setPickupTotalCbm(getCbmTotalPickup(detailList));
                pickup.setPickupTotalAmount(getDetailAmount(detailList));
                pickup.setDetailList(detailList);
            }
        }
        return pickup;
    }

    /**
     * Digunakan untuk menginisialisasi nilai awal dari sebuah pengangkutan
     * @param pickup yang akan di init
     * @return PickupFleet
     */
    private Pickup initPickup(Pickup pickup) {
        pickup.setPickupTotalCbm(0.0f);
        pickup.setFleetIdNumber("0");
        pickup.setPickupTotalAmount(0);
        return pickup;
    }

    /**
     * Digunakan untuk mendapatkan detail sku yang akan diangkut oleh sebuah pengangkutan.
     * Cara : Dilakukan pengaksesan terhadap sku pada skuList yang masih belum mendapatkan pengangkutan (quantity > 0 ).
     *          Kemudian dilakukan pengaksesan terhadap setiap kendaraan available pada sku tersebut. Jika ditemukan kendaraan dengan cbm >= cbm kendaraan parameter input,
     *          maka akan dilakukan pengecekan apakah space cbm pada kendaraan tersebut( var cbm ) masih bisa menampung sebuah sku. Jika bisa maka sebuah sku akan dimasukkan dalam pengangkutan tersebut.
     * @param skuList list Sku yang akan diproses
     * @param fleet kendaraan yang mengangkut
     * @return List<Detail>
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
                            if(recommendationAlgorithmHelper.formatNormalFloat(cbm+sku.getCbm()) > fleet.getCbmCapacity()){
                                statusBreak = true;
                                break;
                            }else{
                                cbm = recommendationAlgorithmHelper.formatNormalFloat(cbm+sku.getCbm());
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
                detail.setCbmPickup(recommendationAlgorithmHelper.formatNormalFloat(skuAmount * sku.getCbm()));
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

    /**
     * Digunakan untuk melakukan perhitungan terhadap total cbm yang diangkut oleh sebuah pengangkutan.
     * Cara : dilakukan pengaksesan terhadap setiap detail sku, kemudian dilakukan penghitungan dengan mengalikan cbm sku tersebut dengan jumlah yang diangkut.
     * @param detailList berisi list detail pengangkutan yang akan dihitung total cbmnya
     * @return total cbm untuk pengangkutan tertentu
     */
    private Float getCbmTotalPickup(List<Detail> detailList) {
        Float total = 0.0f;

        for(Detail detail : detailList){
            total = recommendationAlgorithmHelper.formatNormalFloat(total+detail.getCbmPickup());
        }
        return total;
    }

    /**
     * Digunakan untuk melakukan penghitungan terhadap jumlah sku yang diangkut oleh sebuah pengangkutan
     * Cara : dilakukan pengaksesan terhadap setiap detai sku, kemudian menghitung keseluruhan jumlah sku
     * @param detailList berisi list detail pengangkutan yang akan dihitung total jumlahnya
     * @return totalPengangkutan
     */
    private Integer getDetailAmount(List<Detail> detailList) {
        Integer counter = 0;

        for(Detail detail : detailList){
            counter+=detail.getPickupAmount();
        }
        return counter;
    }


    /**
     * Digunakan untuk memperbaui data pada sku list. Dengan ini apabila setelah dilakukan pengangkutan (pick up) maka sku list diupdate.
     * Sku yang telah ada di pengangkutan akan dihapus dari sku list.
     * Cara : dilakukan pengaksesan terhadap seluruh sku list. Jika ditemukan sku yang ada pada detailSku (sudah di pick up) maka akan dikurangi jumlahnya sebanyak yang di pickupFleet.
     * @param skuList yang akan di update
     * @param detailList yang sudah di pickupFleet
     */
    private void updateSkuList(List<Sku> skuList, List<Detail> detailList) {
        for(Sku sku: skuList){
            for(Detail detail : detailList){
                if(sku.getId().equals(detail.getSku().getId()) && detail.getPickupAmount()>0){
                    sku.setQuantity(sku.getQuantity()-detail.getPickupAmount());
                }
            }
        }
    }

}
