package com.gdn;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Email {
    private String emailAddressDestination;
    private String emailSubject;
    private String emailBody;
}
