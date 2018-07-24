package com.gdn.cff;

import com.gdn.entity.Cff;
import com.gdn.entity.Warehouse;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;

import java.util.Date;
import java.util.List;

public interface CffService {
    WebResponse<List<CffResponse>> getAllCff();
    WebResponse<CffResponse> getOneCff(String cffId);
    WebResponse<CffResponse> saveCff(Cff cff);
    Cff updateSchedulingStatus(String id);
    List<Warehouse> findDistinctWarehouseAndPickupDateIs(Date pickupDate);
}
