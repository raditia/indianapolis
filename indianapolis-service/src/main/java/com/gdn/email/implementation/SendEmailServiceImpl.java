package com.gdn.email.implementation;

import com.gdn.email.Email;
import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.mapper.LogisticVendorEmailBodyMapper;
import com.gdn.repository.CffRepository;
import com.gdn.repository.PickupDetailRepository;
import com.gdn.repository.PickupFleetRepository;
import com.gdn.helper.DateHelper;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
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
//    @Autowired
//    private CffRepository cffRepository;
//    @Autowired
//    private TemplateEngine emailTemplateEngine;
//    @Autowired
//    private Properties emailProperties;

//    private List<LogisticVendor> getLogisticVendorList(List<PickupFleet> pickupFleetList) {
//        List<LogisticVendor> logisticVendorList = new ArrayList<>();
//        LogisticVendor logisticVendor;
//        for (PickupFleet pickupFleet : pickupFleetList
//                ) {
////            logisticVendor = pickupFleet.getFleet().getLogisticVendor();
//            if (!logisticVendorList.contains(logisticVendor)) {
//                logisticVendorList.add(logisticVendor);
//            }
//        }
//        return logisticVendorList;
//    }
//
//    private List<Merchant> getMerchantList(List<PickupDetail> pickupDetailList) {
//        List<Merchant> merchantList = new ArrayList<>();
//        for (PickupDetail pickupDetail:pickupDetailList
//             ) {
//            if(!merchantList.contains(pickupDetail.getMerchant()))
//                merchantList.add(pickupDetail.getMerchant());
//        }
//        return merchantList;
//    }
//
//    private List<User> getTpList(List<PickupDetail> pickupDetailList) {
//        List<User> tpList = new ArrayList<>();
//        User tp;
//        for (PickupDetail pickupDetail:pickupDetailList){
//            tp = pickupDetail.getCffGood().getCff().getTp();
//            if(!tpList.contains(tp))
//                tpList.add(tp);
//        }
//        return tpList;
//    }
//
//    private String getWarehouseEmail(RecommendationResult recommendationResult) {
//        return recommendationResult.getWarehouse().getEmailAddress();
//    }

//    private List<Context> getWarehouseEmailContent(Warehouse warehouse, String pickupDate, List<PickupFleet> pickupFleetList) {
//        Context context = new Context();
//        context.setVariable("warehouseName", warehouse.getAddress());
//        context.setVariable("pickupDate", pickupDate);
//        context.setVariable("pickupList", pickupFleetList);
//        return Collections.singletonList(context);
//    }

//    private List<Context> getLogisticVendorEmailContent(Warehouse warehouse, LogisticVendor logisticVendor, String pickupDate) {
//        List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupWarehouseAndLogisticVendor(warehouse, logisticVendor);
//        Context context = new Context();
//        context.setVariable("logisticVendorName", logisticVendor.getName());
//        context.setVariable("pickupDate", pickupDate);
//        context.setVariable("warehouseName", warehouse.getAddress());
//        context.setVariable("logisticVendorEmailBodyList", LogisticVendorEmailBodyMapper.toLogisticVendorEmailBodyList(pickupFleetList));
//        return Collections.singletonList(context);
//    }

//    private List<Context> getMerchantEmailContent(Warehouse warehouse, Merchant merchant) {
//        List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupWarehouse(warehouse);
//        List<Context> contextList = new ArrayList<>();
//        for (PickupFleet pickupFleet : pickupFleetList
//             ) {
//            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetAndMerchant(pickupFleet, merchant);
//            populateContext(contextList, pickupDetailList);
//        }
//        return contextList;
//    }

//    private List<Context> getTpEmailContent(Warehouse warehouse, User tp) {
//        List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupWarehouse(warehouse);
//        List<Context> contextList = new ArrayList<>();
//        List<PickupDetail> pickupDetailList;
//        List<Cff> cffList;
//        for (PickupFleet pickupFleet : pickupFleetList){
//            cffList = cffRepository
//                    .findAllByTpAndWarehouseAndUploadedDateBetween(
//                            tp,
//                            warehouse,
//                            DateHelper.setTime(1),
//                            DateHelper.setTime(16));
//            for (Cff cff:cffList){
//                pickupDetailList = pickupDetailRepository.findAllByPickupFleetAndCffGoodCff(pickupFleet, cff);
//                populateContext(contextList, pickupDetailList);
//            }
//        }
//        return contextList;
//    }

    @Override
//    @Async
    public void sendEmail(Pickup pickup) {
        sendEmailToWarehouse(pickup);
        sendEmailToLogisticVendor(pickup);
        sendEmailToMerchant(pickup);
        sendEmailToTradePartnership(pickup);
    }

    private void sendEmailToWarehouse(Pickup pickup){
        String warehouseEmailAddress = pickup.getWarehouse().getEmailAddress();
        String deliveryDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        LOGGER.info("********** EMAIL TO WAREHOUSE **********");
        System.out.println("Warehouse email address : " + warehouseEmailAddress);
        System.out.println("Delivery Date : " + deliveryDate);
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
        System.out.println("\n");
    }

    private void sendEmailToLogisticVendor(Pickup pickup){
        LOGGER.info("********** EMAIL TO LOGISTIC VENDOR **********");
        List<LogisticVendor> logisticVendorList = new ArrayList<>();
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()) {
            if (!logisticVendorList.contains(pickupFleet.getLogisticVendor()))
                logisticVendorList.add(pickupFleet.getLogisticVendor());
        }
        for (LogisticVendor logisticVendor:logisticVendorList) {
            System.out.println();
            System.out.println("Logistic Vendor : " + logisticVendor.getName());
            System.out.println("Logistic Vendor email address : " + logisticVendor.getEmailAddress());
            List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupAndLogisticVendor(pickup, logisticVendor);
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
        }
        System.out.println("\n");
    }

    private void sendEmailToMerchant(Pickup pickup){
        LOGGER.info("********** EMAIL TO MERCHANT **********");
        List<Merchant> merchantList = new ArrayList<>();
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                if(!merchantList.contains(pickupDetail.getMerchant()))
                    merchantList.add(pickupDetail.getMerchant());
            }
        }
        for (Merchant merchant:merchantList){
            System.out.println();
            System.out.println("Merchant email address : " + merchant.getEmailAddress());
            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetPickupAndMerchant(pickup, merchant);
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
        }
    }

    private void sendEmailToTradePartnership(Pickup pickup){
        LOGGER.info("********** EMAIL TO TRADE PARTNERSHIP **********");
        List<User> tpList = new ArrayList<>();
        String pickupDate = new SimpleDateFormat("yyyy-MM-dd").format(pickup.getPickupDate());
        for (PickupFleet pickupFleet:pickup.getPickupFleetList()){
            for (PickupDetail pickupDetail:pickupFleet.getPickupDetailList()){
                if(!tpList.contains(pickupDetail.getCffGood().getCff().getTp()))
                    tpList.add(pickupDetail.getCffGood().getCff().getTp());
            }
        }
        for (User tp:tpList){
            System.out.println();
            System.out.println("TP Email Address : " + tp.getEmailAddress());
            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetPickupAndCffGoodCffTpId(pickup, tp.getId());
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
        }
    }

//    private void populateContext(List<Context> contextList, List<PickupDetail> pickupDetailList) {
//        for (PickupDetail pickupDetail:pickupDetailList){
//            Context context = new Context();
//            context.setVariable("cffId", pickupDetail.getCffGood().getCff().getId());
//            context.setVariable("sku", pickupDetail.getCffGood().getSku());
//            context.setVariable("skuPickupQuantity", pickupDetail.getSkuPickupQuantity());
//            context.setVariable("merchantName", pickupDetail.getMerchant().getName());
//            context.setVariable("skuLength", pickupDetail.getCffGood().getLength());
//            context.setVariable("skuWidth", pickupDetail.getCffGood().getWidth());
//            context.setVariable("skuHeight", pickupDetail.getCffGood().getHeight());
//            context.setVariable("skuWeight", pickupDetail.getCffGood().getWeight());
//            contextList.add(context);
//        }
//    }

//    @Override
//    @Async
//    public void sendEmail(Email email) throws MessagingException, IOException, DocumentException {
//        Session session = Session.getInstance(emailProperties, new Authenticator(){
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(emailAddress, password);
//            }
//        });
//
//        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress(emailAddress, false));
//        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getEmailAddressDestination()));
//        msg.setSubject(email.getEmailSubject());
//
//        String body;
//
//        MimeMultipart mimeMultipart = new MimeMultipart();
//        MimeBodyPart textBodyPart = new MimeBodyPart();
//
//        if(email.getEmailSubject().contains("warehouse")){
//            body = emailTemplateEngine.process("email-warehouse", email.getEmailBodyContextList().get(0));
//            textBodyPart.setContent(body, "text/html");
//            mimeMultipart.addBodyPart(textBodyPart);
//            msg.setContent(mimeMultipart);
//            generatePdf(mimeMultipart, email, "email-warehouse");
//            msg.setContent(mimeMultipart);
//        } else if(email.getEmailSubject().contains("merchant") || email.getEmailSubject().contains("trade")){
//            textBodyPart.setText(email.getEmailBodyText());
//            mimeMultipart.addBodyPart(textBodyPart);
//            msg.setContent(mimeMultipart);
//            generatePdf(mimeMultipart, email, "email-koli-label");
//            msg.setContent(mimeMultipart);
//        } else if(email.getEmailSubject().contains("logistic")){
//            body = emailTemplateEngine.process("email-logistic-vendor", email.getEmailBodyContextList().get(0));
//            textBodyPart.setContent(body, "text/html");
//            mimeMultipart.addBodyPart(textBodyPart);
//            msg.setContent(mimeMultipart);
//            generatePdf(mimeMultipart, email, "email-logistic-vendor");
//            msg.setContent(mimeMultipart);
//        }
//
//        //sends the email
//        Transport.send(msg);
//    }

//    private void generatePdf(MimeMultipart mimeMultipart, Email email, String template) throws IOException, DocumentException, MessagingException {
//        String body;
//        OutputStream outputStream = new ByteArrayOutputStream();
//        ITextRenderer renderer = new ITextRenderer();
//        int i=1;
//        for (Context emailBodyContext:email.getEmailBodyContextList()
//                ) {
//            body = emailTemplateEngine.process(template, emailBodyContext);
//
//            // creating pdf
//            renderer.setDocumentFromString(body);
//            renderer.layout();
//            renderer.createPDF(outputStream);
//            byte[] pdfoutput = ((ByteArrayOutputStream) outputStream).toByteArray();
//            outputStream.close();
//            DataSource source = new ByteArrayDataSource(pdfoutput, "application/pdf");
//
//            // add generated pdf to mime multi part
//            MimeBodyPart pdfBodyPart = new MimeBodyPart();
//            pdfBodyPart.setDataHandler(new DataHandler(source));
//            pdfBodyPart.setFileName(email.getEmailSubject() + "_" + email.getPickupDate() + "_" + i);
//            mimeMultipart.addBodyPart(pdfBodyPart);
//            i++;
//        }
//    }

}
