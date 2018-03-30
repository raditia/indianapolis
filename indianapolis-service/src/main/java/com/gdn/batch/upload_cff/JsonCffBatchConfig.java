package com.gdn.batch.upload_cff;

import com.gdn.upload_cff.UploadCffResponse;
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
    ItemReader<UploadCffResponse> jsonCffReader(Environment environment){
        RestTemplate restTemplate = new RestTemplate();
        return new JsonCffReader(environment.getRequiredProperty(PROPERTY_REST_API_URL), restTemplate);
    }

    @Bean
    ItemProcessor<UploadCffResponse, UploadCffResponse> cffProcessor(){
        return new CffProcessor();
    }

    @Bean
    ItemWriter<UploadCffResponse> jpaCffWriter(){
        return new JpaCffWriter();
    }

    @Bean
    Step jsonCffStep(ItemReader<UploadCffResponse> jsonCffReader,
                     ItemProcessor<UploadCffResponse, UploadCffResponse> cffProcessor,
                     ItemWriter<UploadCffResponse> jpaCffWriter,
                     StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("jsonCffStep")
                .<UploadCffResponse, UploadCffResponse>chunk(10)
                .reader(jsonCffReader)
                .processor(cffProcessor)
                .writer(jpaCffWriter)
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
