package com.gdn.email;

import com.gdn.entity.*;
import com.itextpdf.text.DocumentException;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface SendEmailService {
//    List<LogisticVendor> getLogisticVendorList(List<PickupFleet> pickupFleetList);
//    List<Merchant> getMerchantList(List<PickupDetail> pickupDetailList);
//    List<User> getTpList(List<PickupDetail> pickupDetailList);
//    String getWarehouseEmail(RecommendationResult recommendationResult);
//    List<Context> getWarehouseEmailContent(Warehouse warehouse, String pickupDate, List<PickupFleet> pickupFleetList);
//    List<Context> getLogisticVendorEmailContent(Warehouse warehouse, LogisticVendor logisticVendor, String pickupDates);
//    List<Context> getMerchantEmailContent(Warehouse warehouse, Merchant merchant);
//    List<Context> getTpEmailContent(Warehouse warehouse, User tp);
    void sendEmail(Pickup pickup) throws MessagingException;
}
