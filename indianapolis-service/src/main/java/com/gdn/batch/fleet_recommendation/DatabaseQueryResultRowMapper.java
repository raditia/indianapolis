package com.gdn.batch.fleet_recommendation;

import com.gdn.recommendation.DatabaseQueryAllowedVehicles;
import com.gdn.recommendation.DatabaseQueryCffGoods;
import com.gdn.recommendation.DatabaseQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class DatabaseQueryResultRowMapper implements RowMapper<DatabaseQueryResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueryResultRowMapper.class);
    @Override
    public DatabaseQueryResult mapRow(ResultSet resultSet, int i) throws SQLException {
        return DatabaseQueryResult.builder()
                .cffGoods(DatabaseQueryCffGoods.builder()
                        .id(resultSet.getString("id"))
                        .sku(resultSet.getString("sku"))
                        .cbm(resultSet.getDouble("cbm"))
                        .quantity(resultSet.getInt("quantity"))
                        .build())
                .allowedVehicles(DatabaseQueryAllowedVehicles.builder()
                        .vehicleName(resultSet.getString("vehicle_name"))
                        .cbmCapacity(resultSet.getDouble("cbm_capacity"))
                        .build())
                .build();
    }
}