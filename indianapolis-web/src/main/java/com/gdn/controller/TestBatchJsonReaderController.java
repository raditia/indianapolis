package com.gdn.controller;

import com.gdn.category.CategoryService;
import com.gdn.cff.dummy.DummyCffService;
import com.gdn.cff.good.CffGoodService;
import com.gdn.entity.Cff;
import com.gdn.cff.CffService;
import com.gdn.entity.CffGood;
import com.gdn.entity.HeaderCff;
import com.gdn.header.cff.HeaderCffService;
import com.gdn.upload_cff.UploadCffResponse;
import com.gdn.warehouse.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/api/batch")
public class TestBatchJsonReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBatchJsonReaderController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private CffService cffService;
    @Autowired
    private HeaderCffService headerCffService;
    @Autowired
    private CffGoodService cffGoodService;
    @Autowired
    private DummyCffService dummyCffService;

    @RequestMapping(
            value = "/dummy",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UploadCffResponse> getUploadedCffData(){
        categoryService.saveDefaultCategory();
        warehouseService.addDefaultWarehouseInformation();
        return dummyCffService.getDummyUploadCffResponse();
    }

    @RequestMapping(
            value = "/oke",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map testJobLauncher() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        return cffService.executeBatch();
    }

    @RequestMapping(
            value = "/cff",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Cff> getAllCff(){
        return cffService.getAllCff();
    }

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
