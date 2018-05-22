package com.gdn.controller.merchant;

import com.gdn.entity.Merchant;
import com.gdn.merchant.MerchantService;
import com.gdn.response.MerchantResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<MerchantResponse>> findAllMerchant(){
        return merchantService.getAllMerchant();
    }

}
