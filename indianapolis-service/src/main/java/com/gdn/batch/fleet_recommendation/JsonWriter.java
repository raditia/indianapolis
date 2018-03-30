package com.gdn.batch.fleet_recommendation;

import com.gdn.entity.Fleet;
import com.gdn.fleet.FleetService;
import com.gdn.recommendation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JsonWriter implements ItemWriter<List<Pickup>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriter.class);

    @Override
    public void write(List<? extends List<Pickup>> items) throws Exception {

    }

}
