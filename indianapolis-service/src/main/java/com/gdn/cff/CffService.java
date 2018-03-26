package com.gdn.cff;

import com.gdn.entity.Cff;
import com.gdn.upload_cff.UploadCffResponse;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.stereotype.Service;

import javax.batch.operations.JobStartException;
import java.util.List;
import java.util.Map;

public interface CffService {
    Map executeBatch(UploadCffResponse uploadCffResponse) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobStartException, JobInstanceAlreadyCompleteException;
    List<UploadCffResponse> getUploadCffResponse();
    List<Cff> getAllCff();
}
