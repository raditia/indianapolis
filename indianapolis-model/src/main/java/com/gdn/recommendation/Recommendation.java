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
public class Recommendation {
    private String id;
    private List<Pickup> pickupList;
    private Double cbmTotal;
    private Integer skuAmount;
}
