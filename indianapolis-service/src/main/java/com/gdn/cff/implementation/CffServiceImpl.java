package com.gdn.cff.implementation;

import com.gdn.entity.Cff;
import com.gdn.cff.CffService;
import com.gdn.repository.CffRepository;
import com.gdn.upload_cff.UploadCffResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.batch.operations.JobStartException;
import java.util.Arrays;
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

    private List<UploadCffResponse> uploadCffResponseList;

    @Override
    public Map executeBatch(UploadCffResponse uploadCffResponse) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobStartException, JobInstanceAlreadyCompleteException {
        List<UploadCffResponse> uploadCffResponseList = Arrays.asList(uploadCffResponse);
        this.uploadCffResponseList = uploadCffResponseList;
        try {
            JobParameters cffJobParameters = new JobParametersBuilder()
                    .addLong("time",System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(jsonCffJob, cffJobParameters);
        } catch (JobRestartException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("SPRING BATCH", "OKE");
    }

    @Override
    public List<UploadCffResponse> getUploadCffResponse() {
        return this.uploadCffResponseList;
    }

    @Override
    public List<Cff> getAllCff() {
        return cffRepository.findAll();
    }
}
