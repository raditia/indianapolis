package com.gdn.batch;

import com.gdn.Cff;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JsonCffBatchConfig {

    private static final String PROPERTY_REST_API_URL = "api.url";

    @Bean
    ItemReader<Cff> jsonCffReader(Environment environment, RestTemplate restTemplate){
        return new JsonCffReader(environment.getRequiredProperty(PROPERTY_REST_API_URL), restTemplate);
    }

    @Bean
    ItemProcessor<Cff, Cff> logCffProcessor(){
        return new LogCffProcessor();
    }

    @Bean
    ItemWriter<Cff> logCffWriter(){
        return new LogCffWriter();
    }

    @Bean
    Step jsonCffStep(ItemReader<Cff> jsonCffReader,
                     ItemProcessor<Cff, Cff> logCffProcessor,
                     ItemWriter<Cff> logCffWriter,
                     StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("jsonCffStep")
                .<Cff, Cff>chunk(10)
                .reader(jsonCffReader)
                .processor(logCffProcessor)
                .writer(logCffWriter)
                .build();
    }

    @Bean
    Job jsonCffJob(Step jsonCffStep,
                   JobBuilderFactory jobBuilderFactory){
        return jobBuilderFactory.get("jsonCffJob")
                .incrementer(new RunIdIncrementer())
                .flow(jsonCffStep)
                .end()
                .build();
    }

}
