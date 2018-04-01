package com.gdn.allowed_vehicle.implementation;

import com.gdn.allowed_vehicle.AllowedVehicleService;
import com.gdn.entity.AllowedVehicle;
import com.gdn.entity.PickupPoint;
import com.gdn.repository.AllowedVehicleRepository;
import com.gdn.upload_cff.UploadCffAllowedVehicle;
import com.gdn.upload_cff.UploadCffResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AllowedVehicleServiceImpl implements AllowedVehicleService {

    @Autowired
    private AllowedVehicleRepository allowedVehicleRepository;

    @Override
    public void save(PickupPoint pickupPoint,
                     UploadCffResponse uploadCffResponse) {
        for (UploadCffAllowedVehicle allowedVehicle:uploadCffResponse.getAllowedVehicles()){
            allowedVehicleRepository.save(buildAllowedVehicle(pickupPoint, allowedVehicle));
        }
    }

    private AllowedVehicle buildAllowedVehicle(PickupPoint pickupPoint,
                                               UploadCffAllowedVehicle allowedVehicle){
        return AllowedVehicle.builder()
                .id(UUID.randomUUID().toString())
                .vehicleName(allowedVehicle.getVehicleName())
                .pickupPoint(pickupPoint)
                .build();
    }

}
