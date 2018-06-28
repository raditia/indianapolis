package com.gdn.recommendation_algorithm.implementation;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation.Sku;
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

    private Helper helper = new Helper();

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
    @Override
    public List<Fleet> getMaxThree(List<Sku> skuList) {
        List<Fleet> fleetOnDbList = fleetService.findAllByOrderByCbmCapacityDesc();
        List<Fleet> fleetList = new ArrayList<>();

        Fleet maxFleet = getMax(skuList);
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
     * Digunakan untuk mendapatkan jenis kendaraan untuk melanjutkan proses rekomendasi
     * Cara : Melakukan pengaksesan terhadap masing-masing kendaraan dalam basis data,
     *          jika ditemukan kendaraan dengan cbm <= cbm maxFleet maka akan dilakukan pengaksesan terhadap setiap sku.
     *          Untuk masing-masing kendaraan available pada setiap sku, jika cbm kendaraan available >= kendaraan (dari db)
     *          maka cbm total dari sku tersebut (cbm * quantity) akan ditampung.
     *          Dengan demikian dapat diketahui berapa total cbm yang dapat diangkut oleh jenis kendaraan tertentu(parameter).
     *          Jika cbm tersebut melebihi cbm minimal dari kendaraan, maka pengangkutan selanjutnya menggunakan kendaraan tersebut.
     *          Jika tidak maka proses berlanjut ke kendaraan dengan cbm dibawahnya.
     *
     * @param skuList yang akan diproses
     * @param maxFleet kendaraan dengan cbm max yang akan berangkat
     * @return Fleet
     */
    @Override
    public Fleet getNext(List<Sku> skuList, Fleet maxFleet){
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet fleetTemp;
        Double tempCbm;

        for(Fleet fleet : fleetList){
            if(fleet.getCbmCapacity() <= maxFleet.getCbmCapacity()){
                tempCbm = 0.0;
                for(Sku sku : skuList){
                    for(Vehicle vehicle : sku.getVehicleList()){
                        if(vehicle.getCbmCapacity()>=fleet.getCbmCapacity()){
                            tempCbm += helper.formatNormalFloat(sku.getCbm()*sku.getQuantity());
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
     * Digunakan untuk mendapatkan kendaraan dengan kapasitas cbm satu tingkat diatas cbm kendaraan parameter input.
     * Cara : Melakukan pengaksesan pada setiap data kendaraan db dan membandingkan cbm kendaraan tersebut dengan cbm parameter input.
     *          Fungsi akan me-return kendaraan yang memiliki cbm diatas cbm parameter input
     * Input : List kendaraan dalam db secara Asc berdasarkan cbm nya
     * @param maxFleet kendaraan dengan cbm max yang akan berangkat
     * @return Fleet
     */
    @Override
    public Fleet getUp(Fleet maxFleet) {
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityAsc();

        for (Fleet fleet : fleetList) {
            if (fleet.getCbmCapacity() > maxFleet.getCbmCapacity()){
                return fleet;
            }
        }
        return null;
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
    private Fleet getMax(List<Sku> skuList){
        List<Fleet> fleetList = fleetService.findAllByOrderByCbmCapacityDesc();
        Fleet fleetTemp = new Fleet();
        fleetTemp.setCbmCapacity(0.0f);

        for(Fleet fleet : fleetList){
            if(this.getCbm(fleet, skuList) >= fleet.getMinCbm() && fleet.getCbmCapacity() > fleetTemp.getCbmCapacity()){
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
    private Float getCbm(Fleet fleet, List<Sku> skuList){
        Float cbm = 0.0f;

        for(Sku sku : skuList){
            for(Vehicle vehicle : sku.getVehicleList()){
                if(fleet.getName().equals(vehicle.getName())){
                    cbm += helper.formatNormalFloat(sku.getCbm() * sku.getQuantity());
                }
            }
        }
        return cbm;
    }

}
