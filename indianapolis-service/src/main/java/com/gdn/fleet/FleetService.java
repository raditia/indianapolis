package com.gdn.fleet;

import com.gdn.entity.Fleet;
import com.gdn.response.FleetResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface FleetService {
    List<Fleet> findAllByOrderByCbmCapacityDesc();
    List<Fleet> findAllByOrderByCbmCapacityAsc();
    WebResponse<List<FleetResponse>> findDistinctAllFleetOrderByCbmCapacityDesc();
}
