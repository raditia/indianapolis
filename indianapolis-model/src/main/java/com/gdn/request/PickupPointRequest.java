package com.gdn.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickupPointRequest {
    private String pickupAddress;
    private double latitude, longitude;
    private List<AllowedVehicleRequest> allowedVehicleList;
}
