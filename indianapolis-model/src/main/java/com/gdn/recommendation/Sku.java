package com.gdn.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sku {
    private String id, name;
    private int quantity;
    private double cbm;
    private List<Vehicle> vehicleList;
}
