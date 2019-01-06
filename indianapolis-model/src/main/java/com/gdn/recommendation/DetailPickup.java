package com.gdn.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailPickup {
    private Product product;
    private Float pickupCbm;
    private int pickupAmount;
}
