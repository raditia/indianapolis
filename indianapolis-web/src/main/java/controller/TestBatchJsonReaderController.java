package controller;

import dummymodel.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/person")
public class TestBatchJsonReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBatchJsonReaderController.class);

    @Autowired
    @Qualifier("jsonPersonJob")
    private Job jsonPersonJob;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobParameters newExecution;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Person> getAllPerson(){
        List<Person> personList = new ArrayList<>();
        Person person = new Person("Axell", 20);
        personList.add(person);
        Person person1 = new Person("Komang", 20);
        personList.add(person1);
        Person person2 = new Person("Radit", 21);
        personList.add(person2);
        return personList;
    }

    @RequestMapping(
            value = "/oke",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map testJobLauncher() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LOGGER.info("Job jsonPersonJob started!");
        jobLauncher.run(jsonPersonJob, newExecution);
        LOGGER.info("Job jsonPersonJob stopped!");
        return Collections.singletonMap("nggo ndog??", "raolehhh!!!");
    }

}
