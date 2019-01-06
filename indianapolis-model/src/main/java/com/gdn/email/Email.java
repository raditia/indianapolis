package com.gdn.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.context.Context;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String emailAddressDestination;
    private String emailSubject;
    private String emailBodyTemplate;
    private Context emailContext;
}
