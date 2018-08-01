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
    private CffRepository cffRepository;
    @Autowired
    private TemplateEngine emailTemplateEngine;
    @Autowired
    private Properties emailProperties;

    @Override
    public List<LogisticVendor> getLogisticVendorList(List<PickupFleet> pickupFleetList) {
        List<LogisticVendor> logisticVendorList = new ArrayList<>();
        LogisticVendor logisticVendor;
        for (PickupFleet pickupFleet : pickupFleetList
                ) {
            logisticVendor = pickupFleet.getFleet().getLogisticVendor();
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
            tp = pickupDetail.getCffGood().getCff().getTp();
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
    public List<Context> getWarehouseEmailContent(Warehouse warehouse, String pickupDate, List<PickupFleet> pickupFleetList) {
        Context context = new Context();
        context.setVariable("warehouseName", warehouse.getAddress());
        context.setVariable("pickupDate", pickupDate);
        context.setVariable("pickupList", pickupFleetList);
        return Collections.singletonList(context);
    }

    @Override
    public List<Context> getLogisticVendorEmailContent(Warehouse warehouse, LogisticVendor logisticVendor, String pickupDate) {
        List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupWarehouseAndFleetLogisticVendor(warehouse, logisticVendor);
        Context context = new Context();
        context.setVariable("logisticVendorName", logisticVendor.getName());
        context.setVariable("pickupDate", pickupDate);
        context.setVariable("warehouseName", warehouse.getAddress());
        context.setVariable("logisticVendorEmailBodyList", LogisticVendorEmailBodyMapper.toLogisticVendorEmailBodyList(pickupFleetList));
        return Collections.singletonList(context);
    }

    @Override
    public List<Context> getMerchantEmailContent(Warehouse warehouse, Merchant merchant) {
        List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupWarehouse(warehouse);
        List<Context> contextList = new ArrayList<>();
        for (PickupFleet pickupFleet : pickupFleetList
             ) {
            List<PickupDetail> pickupDetailList = pickupDetailRepository.findAllByPickupFleetAndMerchant(pickupFleet, merchant);
            populateContext(contextList, pickupDetailList);
        }
        return contextList;
    }

    @Override
    public List<Context> getTpEmailContent(Warehouse warehouse, User tp) {
        List<PickupFleet> pickupFleetList = pickupFleetRepository.findAllByPickupWarehouse(warehouse);
        List<Context> contextList = new ArrayList<>();
        List<PickupDetail> pickupDetailList;
        List<Cff> cffList;
        for (PickupFleet pickupFleet : pickupFleetList){
            cffList = cffRepository.findAllByTpAndPickupDateAndWarehouse(tp, DateHelper.tomorrow(), warehouse);
            for (Cff cff:cffList){
                pickupDetailList = pickupDetailRepository.findAllByPickupFleetAndCffGoodCff(pickupFleet, cff);
                populateContext(contextList, pickupDetailList);
            }
        }
        return contextList;
    }

    private void populateContext(List<Context> contextList, List<PickupDetail> pickupDetailList) {
        for (PickupDetail pickupDetail:pickupDetailList){
            Context context = new Context();
            context.setVariable("cffId", pickupDetail.getCffGood().getCff().getId());
            context.setVariable("sku", pickupDetail.getCffGood().getSku());
            context.setVariable("skuPickupQuantity", pickupDetail.getSkuPickupQuantity());
            context.setVariable("merchantName", pickupDetail.getMerchant().getName());
            context.setVariable("skuLength", pickupDetail.getCffGood().getLength());
            context.setVariable("skuWidth", pickupDetail.getCffGood().getWidth());
            context.setVariable("skuHeight", pickupDetail.getCffGood().getHeight());
            context.setVariable("skuWeight", pickupDetail.getCffGood().getWeight());
            contextList.add(context);
        }
    }

    @Override
    @Async
    public void sendEmail(Email email) throws MessagingException, IOException, DocumentException {
        Session session = Session.getInstance(emailProperties, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailAddress, false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getEmailAddressDestination()));
        msg.setSubject(email.getEmailSubject());

        String body;

        MimeMultipart mimeMultipart = new MimeMultipart();
        MimeBodyPart textBodyPart = new MimeBodyPart();

        if(email.getEmailSubject().contains("warehouse")){
            body = emailTemplateEngine.process("email-warehouse", email.getEmailBodyContextList().get(0));
            textBodyPart.setContent(body, "text/html");
            mimeMultipart.addBodyPart(textBodyPart);
            msg.setContent(mimeMultipart);
            generatePdf(mimeMultipart, email, "email-warehouse");
            msg.setContent(mimeMultipart);
        } else if(email.getEmailSubject().contains("merchant") || email.getEmailSubject().contains("trade")){
            textBodyPart.setText(email.getEmailBodyText());
            mimeMultipart.addBodyPart(textBodyPart);
            msg.setContent(mimeMultipart);
            generatePdf(mimeMultipart, email, "email-koli-label");
            msg.setContent(mimeMultipart);
        } else if(email.getEmailSubject().contains("logistic")){
            body = emailTemplateEngine.process("email-logistic-vendor", email.getEmailBodyContextList().get(0));
            textBodyPart.setContent(body, "text/html");
            mimeMultipart.addBodyPart(textBodyPart);
            msg.setContent(mimeMultipart);
            generatePdf(mimeMultipart, email, "email-logistic-vendor");
            msg.setContent(mimeMultipart);
        }

        //sends the email
        Transport.send(msg);
    }

    private void generatePdf(MimeMultipart mimeMultipart, Email email, String template) throws IOException, DocumentException, MessagingException {
        String body;
        OutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        int i=1;
        for (Context emailBodyContext:email.getEmailBodyContextList()
                ) {
            body = emailTemplateEngine.process(template, emailBodyContext);

            // creating pdf
            renderer.setDocumentFromString(body);
            renderer.layout();
            renderer.createPDF(outputStream);
            byte[] pdfoutput = ((ByteArrayOutputStream) outputStream).toByteArray();
            outputStream.close();
            DataSource source = new ByteArrayDataSource(pdfoutput, "application/pdf");

            // add generated pdf to mime multi part
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(source));
            pdfBodyPart.setFileName(email.getEmailSubject() + "_" + email.getPickupDate() + "_" + i);
            mimeMultipart.addBodyPart(pdfBodyPart);
            i++;
        }
    }

}
