package com.gdn.fleet.implementation;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.repository.FleetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FleetServiceImpl implements FleetService {

    @Autowired
    private FleetRepository fleetRepository;

    @Override
    public List<Fleet> findAllByOrderByCbmCapacityDesc() {
        return fleetRepository.findAllByOrderByCbmCapacityDesc();
    }

    @Override
    public List<Fleet> findDistinctByNameOrderByCbmCapacityDesc() {
        return fleetRepository.findDistinctByNameOrderByCbmCapacityDesc();
    }

}
