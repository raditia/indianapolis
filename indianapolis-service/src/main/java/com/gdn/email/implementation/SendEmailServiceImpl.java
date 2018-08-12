package com.gdn.email.implementation;

import com.gdn.email.Email;
import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.repository.PickupDetailRepository;
import com.gdn.repository.PickupFleetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailServiceImpl.class.getName());

    @Value("${email.address}")
    private String emailAddress;
    @Value("${email.password}")
    private String password;

    @Autowired
    private PickupFleetRepository pickupFleetRepository;
    @Autowired
    private PickupDetailRepository pickupDetailRepository;
    @Autowired
    private TemplateEngine emailTemplateEngine;
    @Autowired
    private Properties emailProperties;

    @Override
    public void sendEmail(Pickup pickup) throws MessagingException {
        sendEmailToWarehouse(pickup);
        sendEmailToLogisticVendor(pickup);
        sendEmailToMerchant(pickup);
        sendEmailToTradePartnership(pickup);
    }

    private void sendEmailToWarehouse(Pickup pickup) throws MessagingException {
        Email email = new Email();
        Context context = new Context();
        String warehouseEmailAddress = pickup.getWarehouse().getEmailAddress();
        String deliveryDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        LOGGER.info("********** EMAIL TO WAREHOUSE **********");
        context.setVariable("warehouseName", pickup.getWarehouse().getAddress());
        System.out.println("Warehouse email address : " + warehouseEmailAddress);
        email.setEmailAddressDestination(warehouseEmailAddress);
        email.setEmailSubject("Fulfillment by Blibli - Warehouse");
        context.setVariable("deliveryDate", deliveryDate);
        System.out.println("Delivery Date : " + deliveryDate);
        context.setVariable("pickupFleetList", pickup.getPickupFleetList());
        email.setEmailBodyTemplate("email-warehouse");
        email.setEmailContext(context);
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            System.out.println();
            System.out.println("Delivery Details :");
            System.out.println("Logistic Vendors : " + pickupFleet.getLogisticVendor().getName());
            System.out.println("\tFleet : " + pickupFleet.getFleet().getName());
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                System.out.println("\t\tProducts :");
                System.out.println("\t\t\tSKU : " + pickupDetail.getCffGood().getSku());
                System.out.println("\t\t\tQuantity : " + pickupDetail.getSkuPickupQuantity());
            }
        }
        System.out.println("SEND EMAIL TO WAREHOUSE............");
        sendEmail(email);
        System.out.println("\n");
    }

    private void sendEmailToLogisticVendor(Pickup pickup) throws MessagingException {
        Context context = new Context();
        Email email = new Email();
        LOGGER.info("********** EMAIL TO LOGISTIC VENDOR **********");
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        List<LogisticVendor> logisticVendorList = new ArrayList<>();
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()) {
            if (!logisticVendorList.contains(pickupFleet.getLogisticVendor()))
                logisticVendorList.add(pickupFleet.getLogisticVendor());
        }
        context.setVariable("pickupDate", pickupDate);
        for (LogisticVendor logisticVendor:logisticVendorList) {
            System.out.println();
            System.out.println("Logistic Vendor : " + logisticVendor.getName());
            System.out.println("Logistic Vendor email address : " + logisticVendor.getEmailAddress());
            email.setEmailAddressDestination(logisticVendor.getEmailAddress());
            email.setEmailSubject("Fulfillment by Blibli - Logistic Vendor");
            List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupAndLogisticVendor(pickup, logisticVendor);
            context.setVariable("pickupFleetList", pickupFleetList);
            email.setEmailBodyTemplate("email-logistic-vendor");
            email.setEmailContext(context);
            for (PickupFleet pickupFleet:pickupFleetList){
                System.out.println();
                System.out.println("Pickup Details : ");
                System.out.println("\tFleet : " + pickupFleet.getFleet().getName());
                for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()) {
                    System.out.println("\t\tFleet pickup location : " + pickupDetail.getPickupPoint().getPickupAddress());
                    System.out.println("\t\tMerchant details : ");
                    System.out.println("\t\t\tName : " + pickupDetail.getMerchant().getName());
                    System.out.println("\t\t\tPhone : " + pickupDetail.getMerchant().getPhoneNumber());
                    System.out.println("\t\tProduct details : ");
                    System.out.println("\t\t\tSKU : " + pickupDetail.getCffGood().getSku());
                    System.out.println("\t\t\tQuantity : " + pickupDetail.getSkuPickupQuantity());
                }
            }
            System.out.println("SEND EMAIL TO LOGISTIC VENDOR " + logisticVendor.getName() + "............");
            sendEmail(email);
        }
        System.out.println("\n");
    }

    private void sendEmailToMerchant(Pickup pickup) throws MessagingException {
        Context context = new Context();
        Email email = new Email();
        LOGGER.info("********** EMAIL TO MERCHANT **********");
        List<Merchant> merchantList = new ArrayList<>();
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                if(!merchantList.contains(pickupDetail.getMerchant()))
                    merchantList.add(pickupDetail.getMerchant());
            }
        }
        context.setVariable("pickupDate", pickupDate);
        for (Merchant merchant:merchantList){
            System.out.println();
            System.out.println("Merchant email address : " + merchant.getEmailAddress());
            email.setEmailAddressDestination(merchant.getEmailAddress());
            email.setEmailSubject("Fulfillment by Blibli - Merchant");
            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetPickupAndMerchant(pickup, merchant);
            context.setVariable("pickupDetailList", pickupDetailList);
            email.setEmailBodyTemplate("email-koli-label");
            email.setEmailContext(context);
            for (PickupDetail pickupDetail:pickupDetailList){
                System.out.println("Pickup details : ");
                System.out.println("\tPickup Date : " + pickupDate);
                System.out.println("\tSKU : " + pickupDetail.getCffGood().getSku());
                System.out.println("\tQuantity : " + pickupDetail.getSkuPickupQuantity());
                System.out.println("\tPicked up by : ");
                System.out.println("\t\tFleet : " + pickupDetail.getPickupFleet().getFleet().getName());
                System.out.println("\t\tLogistic Vendor : " + pickupDetail.getPickupFleet().getLogisticVendor().getName());
            }
            System.out.println("SEND EMAIL TO MERCHANT " + merchant.getName() + "............");
            sendEmail(email);
        }
    }

    private void sendEmailToTradePartnership(Pickup pickup) throws MessagingException {
        Context context = new Context();
        Email email = new Email();
        LOGGER.info("********** EMAIL TO TRADE PARTNERSHIP **********");
        List<User> tpList = new ArrayList<>();
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                if(!tpList.contains(pickupDetail.getCffGood().getCff().getTp()))
                    tpList.add(pickupDetail.getCffGood().getCff().getTp());
            }
        }
        context.setVariable("pickupDate", pickupDate);
        for (User tp:tpList){
            System.out.println();
            System.out.println("TP Email Address : " + tp.getEmailAddress());
            email.setEmailAddressDestination(tp.getEmailAddress());
            email.setEmailSubject("Fulfillment by Blibli - Trade Partnership");
            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetPickupAndCffGoodCffTpId(pickup, tp.getId());
            context.setVariable("pickupDetailList", pickupDetailList);
            email.setEmailBodyTemplate("email-koli-label");
            email.setEmailContext(context);
            for (PickupDetail pickupDetail:pickupDetailList){
                System.out.println("Pickup Details :");
                System.out.println("\tPickup Date : " + pickupDate);
                System.out.println("\tMerchant : " + pickupDetail.getMerchant().getName());
                System.out.println("\t\tSKU : " + pickupDetail.getCffGood().getSku());
                System.out.println("\t\tQuantity : " + pickupDetail.getSkuPickupQuantity());
                System.out.println("\t\tPicked up by : ");
                System.out.println("\t\t\tFleet : " + pickupDetail.getPickupFleet().getFleet().getName());
                System.out.println("\t\t\tLogistic Vendor : " + pickupDetail.getPickupFleet().getLogisticVendor().getName());
            }
            System.out.println("SEND EMAIL TO TRADE PARTNERSHIP " + tp.getName() + "............");
            sendEmail(email);
        }
    }

    @Async
    public void sendEmail(Email email) throws MessagingException {
        Session session = Session.getInstance(emailProperties, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailAddress, false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getEmailAddressDestination()));
        msg.setSubject(email.getEmailSubject());

        MimeMultipart mimeMultipart = new MimeMultipart();
        MimeBodyPart textBodyPart = new MimeBodyPart();

        String body = emailTemplateEngine.process(email.getEmailBodyTemplate(), email.getEmailContext());
        textBodyPart.setContent(body, "text/html");
        mimeMultipart.addBodyPart(textBodyPart);
        msg.setContent(mimeMultipart);
        Transport.send(msg);
    }

}
