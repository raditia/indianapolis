package com.gdn.controller.fleet;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class FleetController {

    @Autowired
    private FleetService fleetService;

    @RequestMapping(
            value = "/fleet",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Fleet> findAll(){
        return fleetService.findAllByOrderByCbmCapacityDesc();
    }

}
