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
public class Product {
    private String id, name;
    private int quantity;
    private int quantityForLaff;
    private double width;
    private double length;
    private double height;
    private double weight;
    private Float cbm;
    private List<Vehicle> vehicleList;
    private String merchantId, pickupPointId;
}
