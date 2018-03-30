package com.gdn.recommendation;

import com.gdn.entity.Fleet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BestFitFleet {
    private Fleet fleet;
    private int amount;
}
