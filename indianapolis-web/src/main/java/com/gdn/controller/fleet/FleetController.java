package com.gdn.controller.fleet;

import com.gdn.fleet.FleetService;
import com.gdn.response.FleetResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/fleet")
public class FleetController {

    @Autowired
    private FleetService fleetService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<FleetResponse>> findAllFleetDistinctOrderByCbmCapacityDesc(){
        return fleetService.findDistinctAllFleetOrderByCbmCapacityDesc();
    }

}
