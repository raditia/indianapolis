package com.gdn.batch.fleet_recommendation;

import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
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
import java.util.Calendar;
import java.util.List;

@Configuration
public class FleetRecommendationBatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleetRecommendationBatchConfig.class);

    private static final String query = "SELECT " +
            "cff_good.id AS cff_good_id," +
            "cff_good.sku," +
            "cff_good.cbm," +
            "cff_good.quantity," +
            "allowed_vehicle.vehicle_name," +
            "fleet.cbm_capacity," +
            "cff.warehouse_id AS warehouse_id," +
            "merchant.id AS merchant_id, " +
            "pickup_point.id AS pickup_point_id " +
            "from " +
            "cff_good," +
            "allowed_vehicle," +
            "cff," +
            "header_cff," +
            "pickup_point," +
            "fleet," +
            "merchant " +
            "WHERE " +
            "cff_good.cff_id=cff.id AND " +
            "cff.header_cff_id=header_cff.id AND " +
            "pickup_point.header_cff_id=header_cff.id AND " +
            "allowed_vehicle.pickup_point_id=pickup_point.id AND " +
            "allowed_vehicle.vehicle_name=fleet.name AND " +
            "header_cff.id=pickup_point.header_cff_id AND " +
            "pickup_point.merchant_id=merchant.id AND " +
            "cff.warehouse_id=? AND header_cff.date_uploaded=?";

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DatabaseQueryResultRowMapper databaseQueryResultRowMapper;

    @Bean(destroyMethod = "")
    @StepScope
    public ItemStreamReader<DatabaseQueryResult> dbReader(@Value("#{jobParameters['warehouse']}") String warehouseId){
        LOGGER.info("Reading...");
        JdbcCursorItemReader<DatabaseQueryResult> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql(query);
        warehouseId = warehouseId.replace("\'", "");
        String finalWarehouseId = warehouseId;
        reader.setPreparedStatementSetter(new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, finalWarehouseId);
                ps.setTimestamp(2, new Timestamp(tomorrow().getTime()));
            }
        });
        reader.setRowMapper(databaseQueryResultRowMapper);
        return reader;
    }

        private java.util.Date tomorrow(){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        }

    @Bean(destroyMethod = "")
    @StepScope
    public ItemProcessor<DatabaseQueryResult, List<Recommendation>> dbQueryResultProcessor(@Value("#{jobParameters['warehouse']}") String warehouseId){
        warehouseId = warehouseId.replace("\'", "");
        String finalWarehouseId = warehouseId;
        return new DatabaseQueryResultProcessor(finalWarehouseId);
    }

    @Bean
    public ItemWriter<List<Recommendation>> jsonWriter(){
        return new RecommendationResultWriter();
    }

    @Bean
    public Step fleetRecommendationStep(ItemStreamReader<DatabaseQueryResult> dbReader,
                                        ItemProcessor<DatabaseQueryResult, List<Recommendation>> dbQueryResultProcessor,
                                        ItemWriter<List<Recommendation>> jsonWriter,
                                        StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("fleetRecommendationStep")
                .<DatabaseQueryResult, List<Recommendation>>chunk(50)
                .reader(dbReader)
                .processor(dbQueryResultProcessor)
                .writer(jsonWriter)
                .build();
    }

    @Bean
    public Job fleetRecommendationJob(Step fleetRecommendationStep,
                                      JobBuilderFactory jobBuilderFactory,
                                      FleetRecommendationJobListener fleetRecommendationJobListener){
        return jobBuilderFactory.get("fleetRecommendationJob")
                .listener(fleetRecommendationJobListener)
                .incrementer(new RunIdIncrementer())
                .flow(fleetRecommendationStep)
                .end()
                .build();
    }

}
