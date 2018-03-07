package com.gdn.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class LogPersonWriter implements ItemWriter<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPersonWriter.class);

    @Override
    public void write(List<? extends String> list) throws Exception {
        for (String person:list
             ) {
            LOGGER.info("Writing " + person);
        }
    }

}
