package com.gdn.warehouse;

import com.gdn.response.WarehouseResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface WarehouseService {
    WebResponse<List<WarehouseResponse>> findAllWarehouse();
}
