package com.gdn.cff.implementation;

import com.gdn.SchedulingStatus;
import com.gdn.entity.*;
import com.gdn.cff.CffService;
import com.gdn.repository.CffRepository;
import com.gdn.repository.MerchantRepository;
import com.gdn.repository.PickupPointRepository;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;
import com.gdn.mapper.CffResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class CffServiceImpl implements CffService {

    @Autowired
    private CffRepository cffRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private PickupPointRepository pickupPointRepository;

    @Override
    public WebResponse<List<CffResponse>> getAllCff() {
        return WebResponse.OK(CffResponseMapper.toCffListResponse(cffRepository.findAllByOrderByWarehouseAsc()));
    }

    @Override
    public WebResponse<CffResponse> getOneCff(String cffId) {
        try {
            Cff cff = cffRepository.getOne(cffId);
            return WebResponse.OK(CffResponseMapper.toCffResponse(cff));
        } catch (EntityNotFoundException ex){
            return WebResponse.NOT_FOUND();
        }
    }

    private String getMerchantId(Merchant merchant){
        Merchant newMerchant = merchantRepository.findByEmailAddress(merchant.getEmailAddress());
        if(newMerchant!=null)
            return newMerchant.getId();
        else
            return "merchant_" + UUID.randomUUID().toString();
    }

    private String getPickupPointId(PickupPoint pickupPoint){
        PickupPoint newPickupPoint = pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude(
                pickupPoint.getPickupAddress(),
                pickupPoint.getLatitude(),
                pickupPoint.getLongitude()
        );
        if(newPickupPoint!=null)
            return newPickupPoint.getId();
        else
            return "pickup_point_" + UUID.randomUUID().toString();
    }

    @Override
    public WebResponse<CffResponse> saveCff(Cff cff) {
        cff.setUploadedDate(new Date());

        cff.getMerchant().setId(getMerchantId(cff.getMerchant()));

        cff.setSchedulingStatus(SchedulingStatus.PENDING);

        for (CffGood cffGood:cff.getCffGoodList()
             ) {
            cffGood.setId("cff_good_" + UUID.randomUUID().toString());
            cffGood.setCff(cff);
        }

        cff.getPickupPoint().setId(getPickupPointId(cff.getPickupPoint()));

        for (AllowedVehicle allowedVehicle:cff.getPickupPoint().getAllowedVehicleList()
                ) {
            allowedVehicle.setId("allowed_vehicle_" + UUID.randomUUID().toString());
            allowedVehicle.setPickupPoint(cff.getPickupPoint());
        }

        return WebResponse.OK(CffResponseMapper.toCffResponse(cffRepository.save(cff)));
    }

    @Override
    public Cff updateSchedulingStatus(String id) {
        Cff cff = cffRepository.getOne(id);
        cff.setSchedulingStatus(SchedulingStatus.DONE);
        return cffRepository.save(cff);
    }

}
