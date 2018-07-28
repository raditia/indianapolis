package com.gdn.batch.fleet_recommendation;

import com.gdn.SchedulingStatus;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Recommendation;
import com.gdn.helper.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Configuration
public class FleetRecommendationBatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleetRecommendationBatchConfig.class);

    @Value(value = "${recommendation.read.query}")
    private String query;

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;
    @Autowired
    private FleetRecommendationRowMapper fleetRecommendationRowMapper;

    @Bean
    @JobScope
    public JdbcCursorItemReader<DatabaseQueryResult> dbReader(@Value("#{jobParameters['warehouse']}") String warehouseId){
        LOGGER.info("Reading from db...");
        JdbcCursorItemReader<DatabaseQueryResult> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql(query);
        reader.setPreparedStatementSetter(new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, warehouseId);
                ps.setTimestamp(2, new Timestamp(DateHelper.tomorrow().getTime()));
                ps.setString(3, SchedulingStatus.PENDING);
            }
        });
        reader.setRowMapper(fleetRecommendationRowMapper);
        return reader;
    }

    @Bean
    @JobScope
    public ItemProcessor<DatabaseQueryResult, List<Recommendation>> dbQueryResultProcessor(@Value("#{jobParameters['warehouse']}") String warehouseId,
                                                                                           @Value("#{jobParameters['rowCount']}") String rowCount){
        return new FleetRecommendationProcessor(warehouseId, rowCount);
    }

    @Bean
    public ItemWriter<List<Recommendation>> dbWriter(){
        return new FleetRecommendationWriter();
    }

    @Bean
    public Step fleetRecommendationStep(ItemReader<DatabaseQueryResult> dbReader,
                                        ItemProcessor<DatabaseQueryResult, List<Recommendation>> dbQueryResultProcessor,
                                        ItemWriter<List<Recommendation>> dbWriter,
                                        StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("fleetRecommendationStep")
                .<DatabaseQueryResult, List<Recommendation>>chunk(50)
                .reader(dbReader)
                .processor(dbQueryResultProcessor)
                .writer(dbWriter)
                .build();
    }

    @Bean
    public Job fleetRecommendationJob(Step fleetRecommendationStep,
                                      JobBuilderFactory jobBuilderFactory,
                                      FleetRecommendationJobExecutionListener fleetRecommendationJobExecutionListener){
        return jobBuilderFactory.get("fleetRecommendationJob")
                .incrementer(new RunIdIncrementer())
                .listener(fleetRecommendationJobExecutionListener)
                .flow(fleetRecommendationStep)
                .end()
                .build();
    }

}
