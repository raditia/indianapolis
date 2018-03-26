package com.gdn.controller;

import com.gdn.cff.good.CffGoodService;
import com.gdn.entity.CffGood;
import com.gdn.entity.HeaderCff;
import com.gdn.header.cff.HeaderCffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class TestBatchJsonReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBatchJsonReaderController.class);

    @Autowired
    private HeaderCffService headerCffService;
    @Autowired
    private CffGoodService cffGoodService;

    @RequestMapping(
            value = "/header",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<HeaderCff> getAllHeaderCff(){
        return headerCffService.findAll();
    }

    @RequestMapping(
            value = "/goods",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<CffGood> getAllCffGoods(){
        return cffGoodService.findAll();
    }

}
