package com.gdn.email.implementation;

import com.gdn.email.Email;
import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.repository.PickupDetailRepository;
import com.gdn.repository.PickupFleetRepository;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SendEmailServiceImpl implements SendEmailService {

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
    @Async
    public void sendEmail(Pickup pickup) throws MessagingException {
        sendEmailWarehouse(pickup);
        sendEmailLogisticVendor(pickup);
        sendEmailMerchant(pickup);
        sendEmailTradePartnership(pickup);
    }

    private void sendEmailWarehouse(Pickup pickup) throws MessagingException {
        Context context = new Context();
        String warehouseEmailAddress = pickup.getWarehouse().getEmailAddress();
        String deliveryDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        context.setVariable("warehouseName", pickup.getWarehouse().getAddress());
        context.setVariable("deliveryDate", deliveryDate);
        context.setVariable("pickupFleetList", pickup.getPickupFleetList());
        Email email = Email.builder()
                .emailAddressDestination(warehouseEmailAddress)
                .emailSubject("Fulfillment by Blibli - Warehouse")
                .emailBodyTemplate("email-warehouse")
                .emailContext(context)
                .build();
        sendEmail(email);
    }

    private void sendEmailLogisticVendor(Pickup pickup) throws MessagingException {
        Context context = new Context();
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        context.setVariable("pickupDate", pickupDate);
        List<LogisticVendor> logisticVendorList = getLogisticVendorListOfAPickup(pickup);
        for (LogisticVendor logisticVendor:logisticVendorList) {
            List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupAndLogisticVendor(pickup, logisticVendor);
            context.setVariable("pickupFleetList", pickupFleetList);
            Email email = Email.builder()
                    .emailAddressDestination(logisticVendor.getEmailAddress())
                    .emailSubject("Fulfillment by Blibli - Logistic Vendor")
                    .emailBodyTemplate("email-logistic-vendor")
                    .emailContext(context)
                    .build();
            sendEmail(email);
        }
    }
    private List<LogisticVendor> getLogisticVendorListOfAPickup(Pickup pickup){
        List<LogisticVendor> logisticVendorList = new ArrayList<>();
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()) {
            if (!logisticVendorList.contains(pickupFleet.getLogisticVendor()))
                logisticVendorList.add(pickupFleet.getLogisticVendor());
        }
        return logisticVendorList;
    }

    private void sendEmailMerchant(Pickup pickup) throws MessagingException {
        Context context = new Context();
        List<Merchant> merchantList = getMerchantListOfAPickup(pickup);
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        context.setVariable("pickupDate", pickupDate);
        for (Merchant merchant:merchantList){
            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetPickupAndMerchant(pickup, merchant);
            System.out.println("Merchant : " + merchant.getName());
            for (PickupDetail pickupDetail:pickupDetailList){
                System.out.println("Barang : " + pickupDetail.getCffGood().getSku());
                System.out.println("Pickup qty : " + pickupDetail.getSkuPickupQuantity());
            }
            context.setVariable("pickupDetailList", pickupDetailList);
            Email email = Email.builder()
                    .emailAddressDestination(merchant.getEmailAddress())
                    .emailSubject("Fulfillment by Blibli - Merchant")
                    .emailBodyTemplate("email-koli-label")
                    .emailContext(context)
                    .build();
            sendEmail(email);
        }
    }
    private List<Merchant> getMerchantListOfAPickup(Pickup pickup){
        List<Merchant> merchantList = new ArrayList<>();
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                if(!merchantList.contains(pickupDetail.getMerchant()))
                    merchantList.add(pickupDetail.getMerchant());
            }
        }
        return merchantList;
    }

    private void sendEmailTradePartnership(Pickup pickup) throws MessagingException {
        Context context = new Context();
        List<User> tpList = getTradePartnershipListOfAPickup(pickup);
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        context.setVariable("pickupDate", pickupDate);
        for (User tp:tpList){
            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetPickupAndCffGoodCffTpId(pickup, tp.getId());
            context.setVariable("pickupDetailList", pickupDetailList);
            Email email = Email.builder()
                    .emailAddressDestination(tp.getEmailAddress())
                    .emailSubject("Fulfillment by Blibli - Trade Partnership")
                    .emailBodyTemplate("email-koli-label")
                    .emailContext(context)
                    .build();
            sendEmail(email);
        }
    }
    private List<User> getTradePartnershipListOfAPickup(Pickup pickup){
        List<User> tpList = new ArrayList<>();
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                if(!tpList.contains(pickupDetail.getCffGood().getCff().getTp()))
                    tpList.add(pickupDetail.getCffGood().getCff().getTp());
            }
        }
        return tpList;
    }

    private void sendEmail(Email email) throws MessagingException {
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
