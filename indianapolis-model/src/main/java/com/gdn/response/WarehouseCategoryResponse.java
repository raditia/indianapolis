package com.gdn.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseCategoryResponse {

    public String warehouseId;

    public String warehouseAddress;

    public String categoryId;

    public String categoryName;

}
