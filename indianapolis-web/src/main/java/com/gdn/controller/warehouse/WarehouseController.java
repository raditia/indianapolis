package com.gdn.controller.warehouse;

import com.gdn.response.WarehouseResponse;
import com.gdn.response.WebResponse;
import com.gdn.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<WarehouseResponse>> findAllWarehouse(){
        return warehouseService.findAllWarehouse();
    }

}
