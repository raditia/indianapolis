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
public class Recommendation {
    private String id;
    private List<Fleet> topThreeFleets;
    private List<Product> productList;
    private Float cbmTotal;
    private Integer productAmount;
    private String warehouseId;
}
