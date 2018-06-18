package com.gdn.email.implementation;

import com.gdn.email.Email;
import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.mapper.LogisticVendorEmailBodyMapper;
import com.gdn.repository.CffRepository;
import com.gdn.repository.PickupDetailRepository;
import com.gdn.repository.PickupRepository;
import com.gdn.helper.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

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
    @Autowired
    private TemplateEngine emailTemplateEngine;

    @Override
    public List<LogisticVendor> getLogisticVendorList(List<Pickup> pickupList) {
        List<LogisticVendor> logisticVendorList = new ArrayList<>();
        LogisticVendor logisticVendor;
        for (Pickup pickup : pickupList
                ) {
            logisticVendor = pickup.getFleet().getLogisticVendor();
            if (!logisticVendorList.contains(logisticVendor)) {
                logisticVendorList.add(logisticVendor);
            }
        }
        return logisticVendorList;
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
    public Context getWarehouseEmailContent(Warehouse warehouse, String pickupDate, List<Pickup> pickupList) {
        Context context = new Context();
        context.setVariable("warehouseName", warehouse.getAddress());
        context.setVariable("pickupDate", pickupDate);
        context.setVariable("pickupList", pickupList);
        return context;
    }

    @Override
    public Context getLogisticVendorEmailContent(Warehouse warehouse, LogisticVendor logisticVendor, String pickupDate) {
        List<Pickup> pickupList = pickupRepository.findAllByWarehouseAndFleetLogisticVendor(warehouse, logisticVendor);
        Context context = new Context();
        context.setVariable("logisticVendorName", logisticVendor.getName());
        context.setVariable("pickupDate", pickupDate);
        context.setVariable("warehouseName", warehouse.getAddress());
        context.setVariable("logisticVendorEmailBodyList", LogisticVendorEmailBodyMapper.toLogisticVendorEmailBodyList(pickupList));
        return context;
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

        String body="";
        Context context = email.getEmailBodyContext();

        if(email.getEmailSubject().contains("warehouse")){
            body = emailTemplateEngine.process("email-warehouse", context); //Menspecify body dari email adalah email.html dengan context (yaitu variabel2 yang dilempar tadi)
        } else if(email.getEmailSubject().contains("merchant")){

        } else if(email.getEmailSubject().contains("logistic")){
            body = emailTemplateEngine.process("email-logistic-vendor", context);
        } else if(email.getEmailSubject().contains("trade")){

        }

        msg.setContent(body, "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(body,"text/html");

        //sends the email
        Transport.send(msg);
    }

}
