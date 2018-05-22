package com.gdn.cff.implementation;

import com.gdn.SchedulingStatus;
import com.gdn.entity.*;
import com.gdn.cff.CffService;
import com.gdn.merchant.MerchantService;
import com.gdn.pickup_point.PickupPointService;
import com.gdn.repository.CffRepository;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;
import mapper.CffResponseMapper;
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
    @Autowired
    private PickupPointService pickupPointService;

    @Override
    @Transactional(readOnly = true)
    public WebResponse<List<CffResponse>> getAllCff() {
        return WebResponse.OK(CffResponseMapper.toCffListResponse(cffRepository.findAll()));
    }

    @Override
    public WebResponse<CffResponse> saveCff(Cff cff) {
        cff.setId("cff_" + UUID.randomUUID().toString());
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
        PickupPoint pickupPointInDb = pickupPointService
                .findByPickupAddressOrLatitudeOrLongitude(
                        cff.getPickupPoint().getPickupAddress(),
                        cff.getPickupPoint().getLatitude(),
                        cff.getPickupPoint().getLongitude());
        PickupPoint pickupPoint = cff.getPickupPoint();
        if(pickupPointInDb!=null)
            pickupPoint.setId(pickupPointInDb.getId());
        else
            pickupPoint.setId("pickup_point_" + UUID.randomUUID().toString());
        for (AllowedVehicle allowedVehicle:pickupPoint.getAllowedVehicleList()
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

}
