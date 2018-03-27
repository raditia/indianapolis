package com.gdn.batch.beans;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CffBatchBeans {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "cffJobParameters")
    JobParameters cffJobParameters(){
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(System.currentTimeMillis());
        parameters.put("currentTime", parameter);

        return new JobParameters(parameters);
    }

    @Bean(name = "fleetRecommendationJobParameters")
    JobParameters fleetRecommendationJobParameters(){
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(System.currentTimeMillis());
        parameters.put("currentTime", parameter);

        return new JobParameters(parameters);
    }
}
