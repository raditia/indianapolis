package com.gdn.batch.fleet_recommendation;

import com.gdn.SchedulingStatus;
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

    private static final String query = "SELECT \n" +
            "cff.id AS cff_id, \n" +
            "cff_good.id AS cff_good_id, \n" +
            "cff_good.sku, \n" +
            "cff_good.cbm, \n" +
            "cff_good.quantity, \n" +
            "allowed_vehicle.vehicle_name, \n" +
            "fleet.cbm_capacity,\n" +
            "cff.warehouse_id AS warehouse_id,\n" +
            "cff.merchant_id AS merchant_id,\n" +
            "pickup_point.id AS pickup_point_id \n" +
            "from \n" +
            "cff_good, \n" +
            "allowed_vehicle, \n" +
            "cff, \n" +
            "pickup_point, \n" +
            "fleet,\n" +
            "merchant\n" +
            "WHERE \n" +
            "cff_good.cff_id=cff.id AND \n" +
            "allowed_vehicle.pickup_point_id=pickup_point.id AND \n" +
            "allowed_vehicle.vehicle_name=fleet.name AND\n" +
            "cff.warehouse_id=? AND cff.pickup_date=? AND cff.status=?";

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
                ps.setString(3, SchedulingStatus.PENDING);
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
                                      JobBuilderFactory jobBuilderFactory){
        return jobBuilderFactory.get("fleetRecommendationJob")
                .incrementer(new RunIdIncrementer())
                .flow(fleetRecommendationStep)
                .end()
                .build();
    }

}
