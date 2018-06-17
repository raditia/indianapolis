package com.gdn.fleet.implementation;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.repository.FleetRepository;
import com.gdn.response.FleetResponse;
import com.gdn.response.WebResponse;
import com.gdn.mapper.FleetResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public WebResponse<List<FleetResponse>> findDistinctAllFleetOrderByCbmCapacityDesc() {
        return WebResponse.OK(FleetResponseMapper.toFleetResponseList(fleetRepository.findAllByOrderByCbmCapacityDesc()));
    }

}
