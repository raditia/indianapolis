package com.gdn.batch.fleet_recommendation;

import com.gdn.cff.good.CffGoodService;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Sku;
import com.gdn.upload_cff.UploadCffResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FleetRecommendationBatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleetRecommendationBatchConfig.class);

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DatabaseQueryResultRowMapper databaseQueryResultRowMapper;
    @Value("${recommendation.database.query}")
    private String databaseQuery;

    @Bean
    public ItemReader<DatabaseQueryResult> dbReader(){
        JdbcCursorItemReader<DatabaseQueryResult> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql(databaseQuery);
        reader.setRowMapper(databaseQueryResultRowMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<DatabaseQueryResult, DatabaseQueryResult> dbQueryResultProcessor(){
        return new DatabaseQueryResultProcessor();
    }

    @Bean
    public ItemWriter<DatabaseQueryResult> jsonWriter(){
        return new JsonWriter();
    }

    @Bean
    public Step fleetRecommendationStep(ItemReader<DatabaseQueryResult> dbReader,
                                        ItemProcessor<DatabaseQueryResult, DatabaseQueryResult> dbQueryResultProcessor,
                                        ItemWriter<DatabaseQueryResult> jsonWriter,
                                        StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("fleetRecommendationStep")
                .<DatabaseQueryResult, DatabaseQueryResult>chunk(100)
                .reader(dbReader)
                .processor(dbQueryResultProcessor)
                .writer(jsonWriter)
                .build();
    }

    @Bean
    public Job fleetRecommendationJob(Step fleetRecommendationStep,
                                      JobBuilderFactory jobBuilderFactory){
        return jobBuilderFactory.get("fleetRecommendationJob")
                .incrementer(new RunIdIncrementer())
                .flow(fleetRecommendationStep)
                .end()
                .build();
    }

}
