package com.gdn.response;

import com.gdn.entity.CffGood;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class CffResponse {

    private String merchantName;

    private List<CffGood> cffGoodList;

    private String pickupPointAddress;

    private double cbmTotal;

    private Date uploadedDate;

    private String warehouseName;

    private String schedulingStatus;

}
