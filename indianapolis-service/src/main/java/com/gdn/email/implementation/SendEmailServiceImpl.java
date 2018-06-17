package com.gdn.email.implementation;

import com.gdn.Email;
import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.repository.CffRepository;
import com.gdn.repository.PickupDetailRepository;
import com.gdn.repository.PickupRepository;
import helper.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Value("${email.address}")
    private String emailAddress;
    @Value("${email.password}")
    private String password;

    @Autowired
    private PickupRepository pickupRepository;
    @Autowired
    private PickupDetailRepository pickupDetailRepository;
    @Autowired
    private CffRepository cffRepository;

    @Override
    public List<String> getLogisticVendorEmailList(List<Pickup> pickupList) {
        List<String> logisticVendorEmailList = new ArrayList<>();
        String logisticEmailAddress;
        for (Pickup pickup : pickupList
                ) {
            logisticEmailAddress = pickup.getFleet().getLogisticVendor().getEmailAddress();
            if (!logisticVendorEmailList.contains(logisticEmailAddress)) {
                logisticVendorEmailList.add(logisticEmailAddress);
            }
        }
        return logisticVendorEmailList;
    }

    @Override
    public List<Merchant> getMerchantList(List<PickupDetail> pickupDetailList) {
        List<Merchant> merchantList = new ArrayList<>();
        for (PickupDetail pickupDetail:pickupDetailList
             ) {
            if(!merchantList.contains(pickupDetail.getMerchant()))
                merchantList.add(pickupDetail.getMerchant());
        }
        return merchantList;
    }

    @Override
    public List<User> getTpList(List<PickupDetail> pickupDetailList) {
        List<User> tpList = new ArrayList<>();
        User tp;
        for (PickupDetail pickupDetail:pickupDetailList){
            tp = pickupDetail.getSku().getCff().getTp();
            if(!tpList.contains(tp))
                tpList.add(tp);
        }
        return tpList;
    }

    @Override
    public String getWarehouseEmail(RecommendationResult recommendationResult) {
        return recommendationResult.getWarehouse().getEmailAddress();
    }

    @Override
    public String getWarehouseEmailContent(Warehouse warehouse) {
        List<Pickup> pickupList = pickupRepository.findAllByWarehouse(warehouse);
        StringBuilder warehouseEmailContent = new StringBuilder();
        for(Pickup pickup:pickupList) {
            warehouseEmailContent
                    .append("Fleet : ").append(pickup.getFleet().getName()).append("\n")
                    .append("Logistic vendor : ").append(pickup.getFleet().getLogisticVendor().getName()).append("\n")
                    .append("SKU : ").append("\n\n");
            for(PickupDetail pickupDetail:pickup.getPickupDetailList()) {
                warehouseEmailContent
                        .append("Name : ").append(pickupDetail.getSku().getSku()).append("\n")
                        .append("Quantity : ").append(pickupDetail.getSkuPickupQuantity()).append("\n")
                        .append("CBM : ").append(pickupDetail.getCbmPickupAmount()).append("\n\n");
            }
        }
        return String.valueOf(warehouseEmailContent);
    }

    @Override
    public String getLogisticVendorEmailContent(Warehouse warehouse) {
        List<Pickup> pickupList = pickupRepository.findAllByWarehouse(warehouse);
        StringBuilder merchantAndTpEmailContent = new StringBuilder();
        // TODO : Rapikan lagi kontennya.. Mungkin bisa plate number per fleet dijadikan sebagai patokan supaya setiap fleet, tidak perlu menyebut data merchant yang sama berkali-kali
        for (Pickup pickup:pickupList
             ) {
            merchantAndTpEmailContent
                    .append("Fleet : ").append(pickup.getFleet().getName()).append("\n")
                    .append("Warehouse destination : ").append(warehouse.getAddress()).append("\n");
            for (PickupDetail pickupDetail:pickup.getPickupDetailList()
                 ) {
                merchantAndTpEmailContent
                        .append("Merchant name : ").append(pickupDetail.getMerchant().getName()).append("\n")
                        .append("Merchant phone : ").append(pickupDetail.getMerchant().getPhoneNumber()).append("\n")
                        .append("Merchant address : ").append(pickupDetail.getPickupPoint().getPickupAddress()).append("\n")
                        .append("SKU : ").append("\n");
                merchantAndTpEmailContent
                        .append("Name : ").append(pickupDetail.getSku().getSku()).append("\n")
                        .append("Quantity : ").append(pickupDetail.getSkuPickupQuantity()).append("\n")
                        .append("CBM : ").append(pickupDetail.getCbmPickupAmount()).append("\n\n");
            }
        }
        return String.valueOf(merchantAndTpEmailContent);
    }

    @Override
    public String getMerchantEmailContent(Warehouse warehouse, Merchant merchant) {
        List<Pickup> pickupList = pickupRepository.findAllByWarehouse(warehouse);
        List<PickupDetail> pickupDetailList;
        StringBuilder merchantEmailContent = new StringBuilder();
        for (Pickup pickup:pickupList) {
            pickupDetailList = pickupDetailRepository.findAllByPickupAndMerchant(pickup, merchant);
            for (PickupDetail pickupDetail:pickupDetailList) {
                merchantEmailContent
                        .append("Merchant name : ").append(pickupDetail.getMerchant().getName()).append("\n")
                        .append("CFF Number : ").append(pickupDetail.getSku().getCff().getId()).append("\n")
                        .append("SKU : ").append(pickupDetail.getSku().getSku()).append("\n")
                        .append("SKU Quantity : ").append(pickupDetail.getSkuPickupQuantity()).append("\n")
                        .append("Width : ").append(pickupDetail.getSku().getWidth()).append("\n")
                        .append("Length : ").append(pickupDetail.getSku().getLength()).append("\n")
                        .append("Height : ").append(pickupDetail.getSku().getHeight()).append("\n")
                        .append("Weight : ").append(pickupDetail.getSku().getWeight()).append("\n\n");
            }
        }
        return String.valueOf(merchantEmailContent);
    }

    @Override
    public String getTpEmailContent(Warehouse warehouse, User tp) {
        List<Pickup> pickupList = pickupRepository.findAllByWarehouse(warehouse);
        List<PickupDetail> pickupDetailList;
        List<Cff> cffList;
        StringBuilder tpEmailContent = new StringBuilder();
        for (Pickup pickup:pickupList){
            cffList = cffRepository.findAllByTpAndPickupDateAndWarehouse(tp, DateHelper.tomorrow(), warehouse);
            for (Cff cff:cffList){
                pickupDetailList = pickupDetailRepository.findAllByPickupAndSkuCff(pickup, cff);
                for (PickupDetail pickupDetail:pickupDetailList){
                    tpEmailContent
                            .append("Merchant name : ").append(pickupDetail.getMerchant().getName()).append("\n")
                            .append("CFF Number : ").append(pickupDetail.getSku().getCff().getId()).append("\n")
                            .append("SKU : ").append(pickupDetail.getSku().getSku()).append("\n")
                            .append("SKU Quantity : ").append(pickupDetail.getSkuPickupQuantity()).append("\n")
                            .append("Width : ").append(pickupDetail.getSku().getWidth()).append("\n")
                            .append("Length : ").append(pickupDetail.getSku().getLength()).append("\n")
                            .append("Height : ").append(pickupDetail.getSku().getHeight()).append("\n")
                            .append("Weight : ").append(pickupDetail.getSku().getWeight()).append("\n\n");
                }
            }
        }
        return String.valueOf(tpEmailContent);
    }

    @Override
    @Async
    public void sendEmail(Email email) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","587");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });

        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(emailAddress, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getEmailAddressDestination()));
        msg.setSubject(email.getEmailSubject());
        msg.setContent(email.getEmailBody(), "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(email.getEmailBody(),"text/html");

        //sends the email
        Transport.send(msg);
    }

}
