package com.gdn.recommendation;

import com.gdn.entity.Fleet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pickup {
    private List<DetailPickup> detailPickupList;
    private Fleet fleet;
    private String fleetIdNumber;
    private int pickupTotalAmount;
    private Float pickupTotalCbm;
}
