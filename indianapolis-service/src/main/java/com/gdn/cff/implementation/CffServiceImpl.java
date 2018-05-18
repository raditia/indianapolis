package com.gdn.cff.implementation;

import com.gdn.SchedulingStatus;
import com.gdn.entity.*;
import com.gdn.cff.CffService;
import com.gdn.merchant.MerchantService;
import com.gdn.repository.CffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CffServiceImpl implements CffService {

    @Autowired
    private CffRepository cffRepository;
    @Autowired
    private MerchantService merchantService;

    @Override
    @Transactional(readOnly = true)
    public List<Cff> getAllCff() {
        return cffRepository.findAll();
    }

    @Override
    public Cff saveCff(Cff cff) {
        cff.setId("cff_" + UUID.randomUUID().toString());
        Merchant merchant = merchantService.getOne(cff.getMerchant().getEmailAddress());
        if(merchant!=null)
            cff.getMerchant().setId(merchant.getId());
        else
            cff.getMerchant().setId("merchant_" + UUID.randomUUID().toString());
        cff.setSchedulingStatus(SchedulingStatus.PENDING);
        for (CffGood cffGood:cff.getCffGoodList()
             ) {
            cffGood.setId("sku_" + UUID.randomUUID().toString());
        }
        for (PickupPoint pickupPoint:cff.getPickupPointList()
             ) {
            pickupPoint.setId("pickup_point_" + UUID.randomUUID().toString());
            for (AllowedVehicle allowedVehicle:pickupPoint.getAllowedVehicleList()
                 ) {
                allowedVehicle.setId("allowed_vehicle_" + UUID.randomUUID().toString());
            }
        }
        return cffRepository.save(cff);
    }

    @Override
    public Cff findById(String id) {
        return cffRepository.getOne(id);
    }

    @Override
    public Cff updateSchedulingStatus(String id) {
        Cff cff = cffRepository.getOne(id);
        if(cff!=null){
            cff.setSchedulingStatus(SchedulingStatus.DONE);
            return cffRepository.save(cff);
        } else{
            return cff;
        }
    }

}
