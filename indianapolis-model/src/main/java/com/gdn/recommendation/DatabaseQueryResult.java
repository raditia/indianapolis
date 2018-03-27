package com.gdn.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseQueryResult {
    private DatabaseQueryCffGoods cffGoods;
    private DatabaseQueryAllowedVehicles allowedVehicles;
}
