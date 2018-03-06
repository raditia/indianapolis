package batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonPersonBatchJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonPersonBatchJobLauncher.class);

    @Autowired
    @Qualifier("jsonPersonJob")
    private Job jsonPersonJob;

    @Autowired
    private JobLauncher jobLauncher;

//    @Scheduled(cron = "${job.cron}")
    public void launchJsonToLogJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LOGGER.info("Job jsonPersonJob started!");
        jobLauncher.run(jsonPersonJob, newExecution());
        LOGGER.info("Job jsonPersonJob stopped!");
    }

    private JobParameters newExecution(){
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(new Date());
        parameters.put("currentTime", parameter);

        return new JobParameters(parameters);
    }

}
