package com.gdn.batch.fleet_recommendation;

import com.gdn.recommendation.DatabaseQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DatabaseQueryResultProcessor implements ItemProcessor<DatabaseQueryResult, DatabaseQueryResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQueryResultProcessor.class);

    @Override
    public DatabaseQueryResult process(DatabaseQueryResult databaseQueryResult) throws Exception {
        return databaseQueryResult;
    }

}
