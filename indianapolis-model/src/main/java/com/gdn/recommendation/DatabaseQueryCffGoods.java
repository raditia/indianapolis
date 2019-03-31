package com.gdn.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseQueryCffGoods {
    private String id, sku;
    private Float cbm;
    private double width;
    private double length;
    private double height;
    private double weight;
    private int quantity;
}
