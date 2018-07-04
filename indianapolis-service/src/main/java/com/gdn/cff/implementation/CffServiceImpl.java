package com.gdn.cff.implementation;

import com.gdn.SchedulingStatus;
import com.gdn.entity.*;
import com.gdn.cff.CffService;
import com.gdn.merchant.MerchantService;
import com.gdn.repository.CffRepository;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;
import com.gdn.mapper.CffResponseMapper;
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
    public WebResponse<List<CffResponse>> getAllCff() {
        return WebResponse.OK(CffResponseMapper.toCffListResponse(cffRepository.findAllByOrderByWarehouseAsc()));
    }

    @Override
    public WebResponse<CffResponse> saveCff(Cff cff) {
        cff.setUploadedDate(new Date());
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
        cff.getPickupPoint().setId("pickup_point_" + UUID.randomUUID().toString());
        for (AllowedVehicle allowedVehicle:cff.getPickupPoint().getAllowedVehicleList()
                ) {
            allowedVehicle.setId("allowed_vehicle_" + UUID.randomUUID().toString());
        }
        return WebResponse.OK(CffResponseMapper.toCffResponse(cffRepository.save(cff)));
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

    @Override
    public List<Warehouse> findDistinctWarehouseAndPickupDateIs(Date pickupDate) {
        return cffRepository.findDistinctWarehouseAndPickupDateIs(pickupDate);
    }

}
