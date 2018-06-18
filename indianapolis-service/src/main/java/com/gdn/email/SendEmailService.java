package com.gdn.email;

import com.gdn.entity.*;
import com.itextpdf.text.DocumentException;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface SendEmailService {
    List<LogisticVendor> getLogisticVendorList(List<Pickup> pickupList);
    List<Merchant> getMerchantList(List<PickupDetail> pickupDetailList);
    List<User> getTpList(List<PickupDetail> pickupDetailList);
    String getWarehouseEmail(RecommendationResult recommendationResult);
    Context getWarehouseEmailContent(Warehouse warehouse, String pickupDate, List<Pickup> pickupList);
    Context getLogisticVendorEmailContent(Warehouse warehouse, LogisticVendor logisticVendor, String pickupDates);
    List<Context> getMerchantEmailContent(Warehouse warehouse, Merchant merchant);
    String getTpEmailContent(Warehouse warehouse, User tp);
    void sendEmail(Email email) throws MessagingException, IOException, DocumentException;
}
