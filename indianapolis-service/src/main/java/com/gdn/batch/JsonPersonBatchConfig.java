package com.gdn.batch;

import com.gdn.dummymodel.Person;
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
public class JsonPersonBatchConfig {

    private static final String PROPERTY_REST_API_URL = "api.url";

    @Bean
    ItemReader<Person> jsonPersonReader(Environment environment, RestTemplate restTemplate){
        return new JsonPersonReader(environment.getRequiredProperty(PROPERTY_REST_API_URL), restTemplate);
    }

    @Bean
    ItemProcessor<Person, String> logPersonProcessor(){
        return new LogPersonProcessor();
    }

    @Bean
    ItemWriter<String> logPersonWriter(){
        return new LogPersonWriter();
    }

    @Bean
    Step jsonPersonStep(ItemReader<Person> jsonPersonReader,
                        ItemProcessor<Person, String> logPersonProcessor,
                        ItemWriter<String> logPersonWriter,
                        StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("jsonPersonStep")
                .<Person, String>chunk(10)
                .reader(jsonPersonReader)
                .processor(logPersonProcessor)
                .writer(logPersonWriter)
                .build();
    }

    @Bean
    Job jsonPersonJob(Step jsonPersonStep,
                      JobBuilderFactory jobBuilderFactory){
        return jobBuilderFactory.get("jsonPersonJob")
                .incrementer(new RunIdIncrementer())
                .flow(jsonPersonStep)
                .end()
                .build();
    }

}
