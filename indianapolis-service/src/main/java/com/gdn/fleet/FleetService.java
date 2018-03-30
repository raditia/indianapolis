package com.gdn.fleet;

import com.gdn.entity.Fleet;

import java.util.List;

public interface FleetService {
    List<Fleet> findAllByOrderByCbmCapacityDesc();
    List<Fleet> findDistinctByNameOrderByCbmCapacityDesc();
}
