package com.gdn.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkuPickup {
    private String sku;
    private int skuPickupQuantity;
    private Float skuPickupCbmAmount;
}
