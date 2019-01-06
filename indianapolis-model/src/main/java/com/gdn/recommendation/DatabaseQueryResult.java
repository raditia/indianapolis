package com.gdn.recommendation;

import com.gdn.entity.Cff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseQueryResult {
    private String cffId;
    private DatabaseQueryCffGoods cffGoods;
    private DatabaseQueryAllowedVehicles allowedVehicles;
    private String warehouseId;
    private String merchantId;
    private String pickupPointId;
}
