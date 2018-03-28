package com.gdn.controller.merchant;

import com.gdn.entity.Merchant;
import com.gdn.merchant.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping(
            value = "/merchant",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Merchant> findAll(){
        return merchantService.findAll();
    }

}
