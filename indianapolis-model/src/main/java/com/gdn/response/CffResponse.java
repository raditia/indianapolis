package com.gdn.response;

import com.gdn.SchedulingStatus;
import com.gdn.entity.CffGood;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class CffResponse {

    private String merchantName;

    private String cffId;

    private List<CffGood> cffGoodList;

    private String pickupPointAddress;

    private Float cbmTotal;

    private Date pickupDate;

    private String warehouseName;

    private SchedulingStatus schedulingStatus;

}
