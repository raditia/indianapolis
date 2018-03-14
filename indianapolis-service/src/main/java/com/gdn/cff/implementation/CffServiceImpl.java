package com.gdn.cff.implementation;

import com.gdn.Cff;
import com.gdn.cff.CffService;
import com.gdn.repository.CffRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.batch.operations.JobStartException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CffServiceImpl implements CffService {


    @Autowired
    private CffRepository cffRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job jsonCffJob;

    @Autowired
    private JobParameters cffJobParameters;

    @Override
    public Map executeBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobStartException, JobInstanceAlreadyCompleteException {
        try {
            jobLauncher.run(jsonCffJob, cffJobParameters);
        } catch (JobRestartException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("SPRING BATCH", "OKE");
    }

    @Override
    public Cff saveCff(Cff cff) {
        return cffRepository.save(cff);
    }

    @Override
    public List<Cff> getAllCff() {
        return cffRepository.findAll();
    }
}
