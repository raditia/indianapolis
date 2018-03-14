package com.gdn.cff;

import com.gdn.Cff;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.stereotype.Service;

import javax.batch.operations.JobStartException;
import java.util.List;
import java.util.Map;

@Service
public interface CffService {
    Map executeBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobStartException, JobInstanceAlreadyCompleteException;
    Cff saveCff(Cff cff);
    List<Cff> getAllCff();
}
