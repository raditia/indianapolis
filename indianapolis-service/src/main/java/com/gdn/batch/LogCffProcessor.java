package com.gdn.batch;

import com.gdn.Cff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LogCffProcessor implements ItemProcessor<Cff, Cff> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogCffProcessor.class);

    @Override
    public Cff process(Cff cff) throws Exception {
        cff.setProductName(cff.getProductName().toUpperCase());
        return cff;
    }

}
