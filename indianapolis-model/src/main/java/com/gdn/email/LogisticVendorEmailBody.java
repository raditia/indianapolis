package com.gdn.email;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LogisticVendorEmailBody {
    private String fleetName;
    private String warehouseDestinationName;
    private String warehouseDestinationPhoneNumber;

    private String merchantName;
    private String merchantPhoneNumber;
    private String merchantAddress;
    private String merchantAddressCoordinate;

    private List<SkuPickup> skuPickupList;
}
