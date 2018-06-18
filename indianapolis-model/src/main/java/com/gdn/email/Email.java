package com.gdn.email;

import com.gdn.entity.PickupDetail;
import com.gdn.entity.Warehouse;
import lombok.Builder;
import lombok.Data;
import org.thymeleaf.context.Context;

import java.util.List;

@Data
@Builder
public class Email {
    private String emailAddressDestination;
    private String emailSubject;
    private Context emailBodyContext;
    private List<Context> emailBodyContextList;
    private String emailBodyText;
    private String pickupDate;
    private List<PickupDetail> pickupDetailList;
    private Warehouse warehouse;
}
