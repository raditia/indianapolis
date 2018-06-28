package com.gdn.controller.warehouse_category;

import com.gdn.response.WarehouseCategoryResponse;
import com.gdn.response.WebResponse;
import com.gdn.warehouse_category.WarehouseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/warehouse-category")
public class WarehouseCategoryController {

    @Autowired
    private WarehouseCategoryService warehouseCategoryService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<WarehouseCategoryResponse>> findAllWarehouseCategory(){
        return warehouseCategoryService.findAllWarehouseCategory();
    }

}
