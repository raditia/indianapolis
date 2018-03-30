package com.gdn.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Detail {
    private Sku sku;
    private double cbmPickup;
    private int pickupAmount;
}
