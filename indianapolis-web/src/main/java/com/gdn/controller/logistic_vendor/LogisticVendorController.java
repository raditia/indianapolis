package com.gdn.controller.logistic_vendor;

import com.gdn.logistic_vendor.LogisticVendorService;
import com.gdn.response.LogisticVendorResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/logistic-vendor")
public class LogisticVendorController {

    @Autowired
    private LogisticVendorService logisticVendorService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<LogisticVendorResponse>> findAllLogisticVendor(){
        return logisticVendorService.findAll();
    }

}
