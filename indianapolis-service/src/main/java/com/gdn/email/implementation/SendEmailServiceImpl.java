package com.gdn.email.implementation;

import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.repository.PickupDetailRepository;
import com.gdn.repository.PickupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private PickupRepository pickupRepository;
    @Autowired
    private PickupDetailRepository pickupDetailRepository;

    @Override
    public List<String> getTpEmailList(List<PickupDetail> pickupDetailList) {
        List<String> tpEmailList = new ArrayList<>();
        String tpEmailAddress;
        for (PickupDetail pickupDetail:pickupDetailList
                ) {
            tpEmailAddress = pickupDetail.getSku().getCff().getTp().getEmailAddress();
            if(!tpEmailList.contains(tpEmailAddress)){
                tpEmailList.add(tpEmailAddress);
            }
        }
        return tpEmailList;
    }

    @Override
    public List<String> getLogisticVendorEmailList(List<Pickup> pickupList) {
        List<String> logisticVendorEmailList = new ArrayList<>();
        String logisticEmailAddress;
        for (Pickup pickup : pickupList
                ) {
            logisticEmailAddress = pickup.getFleet().getLogisticVendor().getEmailAddress();
            if (!logisticVendorEmailList.contains(logisticEmailAddress)) {
                logisticVendorEmailList.add(logisticEmailAddress);
            }
        }
        return logisticVendorEmailList;
    }

    @Override
    public List<String> getMerchantEmailList(List<PickupDetail> pickupDetailList) {
        List<String> merchantEmailList = new ArrayList<>();
        String merchantEmailAddress;
        for (PickupDetail pickupDetail:pickupDetailList
                ) {
            merchantEmailAddress = pickupDetail.getMerchant().getEmailAddress();
            if(!merchantEmailList.contains(merchantEmailAddress)){
                merchantEmailList.add(merchantEmailAddress);
            }
        }
        return merchantEmailList;
    }

    @Override
    public String getWarehouseEmail(RecommendationResult recommendationResult) {
        return recommendationResult.getWarehouse().getEmailAddress();
    }

    @Override
    public String getWarehouseEmailContent(Warehouse warehouse) {
        List<Pickup> pickupList = pickupRepository.findAllByWarehouse(warehouse);
        StringBuilder warehouseEmailContent = new StringBuilder();
        for(Pickup pickup:pickupList) {
            warehouseEmailContent
                    .append("Fleet : ").append(pickup.getFleet().getName()).append("\n")
                    .append("Logistic vendor : ").append(pickup.getFleet().getLogisticVendor().getName()).append("\n")
                    .append("SKU : ").append("\n\n");
            for(PickupDetail pickupDetail:pickup.getPickupDetailList()) {
                warehouseEmailContent
                        .append("Name : ").append(pickupDetail.getSku().getSku()).append("\n")
                        .append("Quantity : ").append(pickupDetail.getSkuPickupQuantity()).append("\n")
                        .append("CBM : ").append(pickupDetail.getCbmPickupAmount()).append("\n\n");
            }
        }
        return String.valueOf(warehouseEmailContent);
    }

    @Override
    public String getLogisticVendorEmailContent(Warehouse warehouse) {
        List<Pickup> pickupList = pickupRepository.findAllByWarehouse(warehouse);
        StringBuilder merchantAndTpEmailContent = new StringBuilder();
        // TODO : Rapikan lagi kontennya.. Mungkin bisa plate number per fleet dijadikan sebagai patokan supaya setiap fleet, tidak perlu menyebut data merchant yang sama berkali-kali
        for (Pickup pickup:pickupList
             ) {
            merchantAndTpEmailContent
                    .append("Fleet : ").append(pickup.getFleet().getName()).append("\n")
                    .append("Warehouse destination : ").append(warehouse.getAddress()).append("\n");
            for (PickupDetail pickupDetail:pickup.getPickupDetailList()
                 ) {
                merchantAndTpEmailContent
                        .append("Merchant name : ").append(pickupDetail.getMerchant().getName()).append("\n")
                        .append("Merchant phone : ").append(pickupDetail.getMerchant().getPhoneNumber()).append("\n")
                        .append("Merchant address : ").append(pickupDetail.getPickupPoint().getPickupAddress()).append("\n")
                        .append("SKU : ").append("\n");
                merchantAndTpEmailContent
                        .append("Name : ").append(pickupDetail.getSku().getSku()).append("\n")
                        .append("Quantity : ").append(pickupDetail.getSkuPickupQuantity()).append("\n")
                        .append("CBM : ").append(pickupDetail.getCbmPickupAmount()).append("\n\n");
            }
        }
        return String.valueOf(merchantAndTpEmailContent);
    }

}
