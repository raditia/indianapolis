package com.gdn.controller.cff;

import com.gdn.cff.CffService;
import com.gdn.entity.Cff;
import com.gdn.request.CffRequest;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CffController {

    @Autowired
    private CffService cffService;

    @RequestMapping(
            value = "/cff",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CffResponse> saveCff(@RequestBody CffRequest cffRequest) {
        return cffService.saveCff(cffRequest);
    }

    @RequestMapping(
            value = "/cff",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<CffResponse>> getAllCff(){
        return cffService.getAllCff();
    }

    @RequestMapping(
            value = "/cff/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CffResponse> getOneCff(@PathVariable(name = "id") String cffId){
        return cffService.getOneCff(cffId);
    }

}
