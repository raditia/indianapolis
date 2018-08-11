package com.gdn.cff.implementation;

import com.gdn.SchedulingStatus;
import com.gdn.entity.*;
import com.gdn.cff.CffService;
import com.gdn.mapper.CffMapper;
import com.gdn.repository.CffRepository;
import com.gdn.repository.MerchantRepository;
import com.gdn.repository.PickupPointRepository;
import com.gdn.request.CffRequest;
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

    @Override
    public WebResponse<CffResponse> saveCff(CffRequest cffRequest) {
        Cff cff = CffMapper.toCff(cffRequest);

        cff.setUploadedDate(new Date());

        cff.getMerchant().setId(getMerchantId(cff.getMerchant()));

        cff.setSchedulingStatus(SchedulingStatus.PENDING);

        for (CffGood cffGood:cff.getCffGoodList()
                ) {
            cffGood.setId("cff_good_" + UUID.randomUUID().toString());
            cffGood.setCff(cff);
        }

        PickupPoint pickupPointInDatabase = findPickupPointInDatabase(cff.getPickupPoint());
        if(pickupPointInDatabase!=null){
            cff.getPickupPoint().setId(pickupPointInDatabase.getId());
            cff.getPickupPoint().setAllowedVehicleList(getExistingAllowedVehicleList(pickupPointInDatabase));
        } else{
            cff.getPickupPoint().setId("pickup_point_" + UUID.randomUUID().toString());
            cff.getPickupPoint().setAllowedVehicleList(getNewAllowedVehicleList(cff.getPickupPoint()));
        }

        return WebResponse.OK(CffResponseMapper.toCffResponse(cffRepository.save(cff)));
    }

    private String getMerchantId(Merchant merchant){
        Merchant newMerchant = merchantRepository.findByEmailAddress(merchant.getEmailAddress());
        if(newMerchant!=null)
            return newMerchant.getId();
        else
            return "merchant_" + UUID.randomUUID().toString();
    }

    private PickupPoint findPickupPointInDatabase(PickupPoint pickupPoint){
        return pickupPointRepository.findByPickupAddressOrLatitudeAndLongitude(
                pickupPoint.getPickupAddress(),
                pickupPoint.getLatitude(),
                pickupPoint.getLongitude()
        );
    }

    private List<AllowedVehicle> getNewAllowedVehicleList(PickupPoint pickupPoint){
        List<AllowedVehicle> allowedVehicleList = new ArrayList<>();
        for (AllowedVehicle allowedVehicle:pickupPoint.getAllowedVehicleList()
                ) {
            allowedVehicle.setId("allowed_vehicle_" + UUID.randomUUID().toString());
            allowedVehicle.setPickupPoint(pickupPoint);
            allowedVehicleList.add(allowedVehicle);
        }
        return allowedVehicleList;
    }

    private List<AllowedVehicle> getExistingAllowedVehicleList(PickupPoint pickupPoint){
        List<AllowedVehicle> allowedVehicleList = new ArrayList<>();
        for (AllowedVehicle allowedVehicle:pickupPoint.getAllowedVehicleList()
                ) {
            allowedVehicle.setId(allowedVehicle.getId());
            allowedVehicle.setPickupPoint(pickupPoint);
            allowedVehicleList.add(allowedVehicle);
        }
        return allowedVehicleList;
    }

    @Override
    public Cff updateSchedulingStatus(String id) {
        Cff cff = cffRepository.getOne(id);
        cff.setSchedulingStatus(SchedulingStatus.DONE);
        return cffRepository.save(cff);
    }

}
