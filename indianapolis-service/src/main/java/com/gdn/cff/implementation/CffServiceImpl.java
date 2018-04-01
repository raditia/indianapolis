package com.gdn.cff.implementation;

import com.gdn.allowed_vehicle.AllowedVehicleService;
import com.gdn.cff.good.CffGoodService;
import com.gdn.entity.*;
import com.gdn.cff.CffService;
import com.gdn.repository.CffRepository;
import com.gdn.upload_cff.UploadCffResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.batch.operations.JobStartException;
import java.util.*;

@Service
@Transactional
public class CffServiceImpl implements CffService {

    @Autowired
    private CffGoodService cffGoodService;
    @Autowired
    private AllowedVehicleService allowedVehicleService;
    @Autowired
    private CffRepository cffRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cff> getAllCff() {
        return cffRepository.findAll();
    }

    @Override
    public UploadCffResponse saveCff(UploadCffResponse uploadCffResponse) {
        Cff cff = buildCff(uploadCffResponse);
        cffRepository.save(cff);
        cffGoodService.save(cff, uploadCffResponse);
        allowedVehicleService.save(buildPickupPoint(uploadCffResponse), uploadCffResponse);
        return uploadCffResponse;
    }

    private Cff buildCff(UploadCffResponse response){
        return Cff.builder()
                .id(response.getRequestor().getId())
                .headerCff(response.getRequestor())
                .category(buildCategory(response))
                .build();
    }

    private Category buildCategory(UploadCffResponse response){
        return Category.builder()
                .id(response.getCategory())
                .build();
    }

    private PickupPoint buildPickupPoint(UploadCffResponse response){
        return PickupPoint.builder()
                .id(UUID.randomUUID().toString())
                .headerCff(response.getRequestor())
                .pickupAddress(response.getPickupPoint().getPickupAddress())
                .latitude(response.getPickupPoint().getLatitude())
                .longitude(response.getPickupPoint().getLongitude())
                .merchant(buildMerchant(response))
                .build();
    }

    private Merchant buildMerchant(UploadCffResponse response){
        return Merchant.builder()
                .id(UUID.randomUUID().toString())
                .name(response.getMerchant().getName())
                .emailAddress(response.getMerchant().getEmailAddress())
                .phoneNumber(response.getMerchant().getPhoneNumber())
                .build();
    }

}
