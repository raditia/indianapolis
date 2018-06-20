package com.gdn.email;

import lombok.Builder;
import lombok.Data;
import org.thymeleaf.context.Context;

import java.util.List;

@Data
@Builder
public class Email {
    private String emailAddressDestination;
    private String emailSubject;
    private List<Context> emailBodyContextList;
    private String emailBodyText;
    private String pickupDate;
}
