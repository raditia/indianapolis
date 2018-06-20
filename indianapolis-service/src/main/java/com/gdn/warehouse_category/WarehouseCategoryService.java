package com.gdn.warehouse_category;

import com.gdn.response.WarehouseCategoryResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface WarehouseCategoryService {
    WebResponse<List<WarehouseCategoryResponse>> findAllWarehouseCategory();
}
