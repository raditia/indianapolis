package com.gdn.email;

import lombok.Builder;
import lombok.Data;
import org.thymeleaf.context.Context;

@Data
@Builder
public class Email {
    private String emailAddressDestination;
    private String emailSubject;
    private Context emailBodyContext;
}
