package com.gdn.batch.upload_cff;

import com.gdn.entity.*;
import com.gdn.upload_cff.UploadCffAllowedVehicle;
import com.gdn.upload_cff.UploadCffGood;
import com.gdn.upload_cff.UploadCffResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

public class JpaCffWriter implements ItemWriter<UploadCffResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaCffWriter.class);

    @Autowired
    private EntityManager entityManager;


    @Override
    public void write(List<? extends UploadCffResponse> list) throws Exception {
        for (UploadCffResponse response:list
             ) {
            logRequestorInformation(response);
            logCffInformation(response);
            Cff cff = buildCff(response);
            insertCff(cff); //insert to table cff and header_cff
            insertCffGoods(cff, response);
            PickupPoint pickupPoint = buildPickupPoint(response);
            insertPickupPoint(pickupPoint);
            insertAllowedVehicle(pickupPoint, response); //insert allowed_vehicle, pickup_point, and merchant (?)
        }
    }

    private void logRequestorInformation(UploadCffResponse response){
        LOGGER.info("\n" +
                "Writing header cff to database...\n" +
                "ID : " + response.getRequestor().getId() + "\n" +
                "Requestor name : " + response.getRequestor().getName() + "\n" +
                "Date uploaded : " + response.getRequestor().getDate() + "\n");
    }

    private void logCffInformation(UploadCffResponse response){
        LOGGER.info("\n" +
                "Writing cff to database...\n" +
                "Category ID : " + response.getCategory() + "\n" +
                "Warehouse ID : " + response.getWarehouse() + "\n" +
                "Header CFF ID : " + response.getRequestor().getId() + "\n");
    }

    private Category buildCategory(UploadCffResponse response){
        return Category.builder()
                .id(response.getCategory())
                .build();
    }

    private Cff buildCff(UploadCffResponse response){
        return Cff.builder()
                .id(response.getRequestor().getId())
                .headerCff(response.getRequestor())
                .category(buildCategory(response))
                .build();
    }

    private void insertCff(Cff cff){
        entityManager.persist(cff);
    }

    private Merchant buildMerchant(UploadCffResponse response){
        return Merchant.builder()
                .id(UUID.randomUUID().toString())
                .name(response.getMerchant().getName())
                .emailAddress(response.getMerchant().getEmailAddress())
                .phoneNumber(response.getMerchant().getPhoneNumber())
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

    private void insertPickupPoint(PickupPoint pickupPoint){
        entityManager.persist(pickupPoint);
    }

    private AllowedVehicle buildAllowedVehicle(PickupPoint pickupPoint,
                                               UploadCffAllowedVehicle allowedVehicle){
        return AllowedVehicle.builder()
                .id(UUID.randomUUID().toString())
                .vehicleName(allowedVehicle.getVehicleName())
                .pickupPoint(pickupPoint)
                .build();
    }

    private void insertAllowedVehicle(PickupPoint pickupPoint,
                                      UploadCffResponse response){
        for (UploadCffAllowedVehicle allowedVehicle:response.getAllowedVehicles()){
            entityManager.persist(buildAllowedVehicle(pickupPoint, allowedVehicle));
        }
    }

    private void insertCffGoods(Cff cff,
                                UploadCffResponse response){
        for (UploadCffGood good:response.getGoods()
                ) {
            logCffGoodInformation(response, good);
            entityManager.persist(buildCffGoods(cff, good));
        }
    }

    private void logCffGoodInformation(UploadCffResponse response,
                                       UploadCffGood good){
        LOGGER.info("\n" +
                "writing cff goods to database...\n" +
                "CFF ID : " + response.getRequestor().getId() + "\n" +
                "SKU : " + good.getSku() + "\n" +
                "CBM : " + good.getCbm() + "\n" +
                "Quantity : " + good.getQuantity());
    }

    private CffGood buildCffGoods(Cff cff,
                               UploadCffGood good){
        return CffGood.builder()
                .id(UUID.randomUUID().toString())
                .cff(cff)
                .sku(good.getSku())
                .cbm(good.getCbm())
                .quantity(good.getQuantity())
                .build();
    }

}
