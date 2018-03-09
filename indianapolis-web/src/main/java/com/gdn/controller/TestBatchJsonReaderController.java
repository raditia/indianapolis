package com.gdn.controller;

import com.gdn.Category;
import com.gdn.Cff;
import com.gdn.HeaderCff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("jsonCffJob")
    private Job jsobCffJob;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobParameters newExecution;

    @RequestMapping(
            value = "/dummy",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Cff> getUploadedCffData(){
        Cff cff1 = Cff.builder()
                .id("id_" + UUID.randomUUID().toString())
                .sku("sku_" + UUID.randomUUID().toString())
                .productName("Sempak Superman")
                .cbm(new Random().nextFloat())
                .quantity(new Random().nextInt())
                .category(Category.builder()
                        .id("category_id_" + UUID.randomUUID().toString())
                        .name("Celana dalam")
                        .build())
                .headerCff(HeaderCff.builder()
                        .id("header_cff_id_" + UUID.randomUUID().toString())
                        .dateUploaded(new Date())
                        .tpName("Komang")
                        .build())
                .build();
        Cff cff2 = Cff.builder()
                .id("id_" + UUID.randomUUID().toString())
                .sku("sku_" + UUID.randomUUID().toString())
                .productName("Sempak Batman")
                .cbm(new Random().nextFloat())
                .quantity(new Random().nextInt())
                .category(Category.builder()
                        .id("category_id_" + UUID.randomUUID().toString())
                        .name("Celana dalam")
                        .build())
                .headerCff(HeaderCff.builder()
                        .id("header_cff_id_" + UUID.randomUUID().toString())
                        .dateUploaded(new Date())
                        .tpName("Radit")
                        .build())
                .build();
        Cff cff3 = Cff.builder()
                .id("id_" + UUID.randomUUID().toString())
                .sku("sku_" + UUID.randomUUID().toString())
                .productName("Sempak Spiderman")
                .cbm(new Random().nextFloat())
                .quantity(new Random().nextInt())
                .category(Category.builder()
                        .id("category_id_" + UUID.randomUUID().toString())
                        .name("Celana dalam")
                        .build())
                .headerCff(HeaderCff.builder()
                        .id("header_cff_id_" + UUID.randomUUID().toString())
                        .dateUploaded(new Date())
                        .tpName("Axell")
                        .build())
                .build();
        List<Cff> cffList = new ArrayList<>();
        cffList.add(cff1); cffList.add(cff2); cffList.add(cff3);
        return cffList;
    }

    @RequestMapping(
            value = "/oke",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map testJobLauncher() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LOGGER.info("Job jsobCffJob started!");
        jobLauncher.run(jsobCffJob, newExecution);
        LOGGER.info("Job jsobCffJob stopped!");
        return Collections.singletonMap("nggo ndog??", "raolehhh!!!");
    }

}
