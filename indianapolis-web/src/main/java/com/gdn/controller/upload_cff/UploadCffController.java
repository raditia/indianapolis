package com.gdn.controller.upload_cff;

import com.gdn.cff.CffService;
import com.gdn.entity.Cff;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UploadCffController {

    @Autowired
    private CffService cffService;

    @RequestMapping(
            value = "/cff",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CffResponse> saveCff(@RequestBody Cff cff) {
        return cffService.saveCff(cff);
    }

    @RequestMapping(
            value = "/cff",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<CffResponse>> getAllCff(){
        return cffService.getAllCff();
    }

}
