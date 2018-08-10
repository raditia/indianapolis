package com.gdn.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CffGoodRequest {
    private String sku;
    private Float length, width, height, weight;
    private Float cbm;
    private int quantity;
}
