package com.gdn.batch;

import com.gdn.Cff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class JsonCffReader implements ItemReader<Cff> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonCffReader.class);

    private List<Cff> cffList;
    private int nextCffIndex;
    private String url;
    private RestTemplate restTemplate;

    public JsonCffReader(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
        nextCffIndex = 0;
    }

    @Override
    public Cff read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (personDataIsNotInitialized()) {
            cffList = fetchCffDataFromApi();
        }

        Cff nextCff = null;

        if (nextCffIndex < cffList.size()) {
            nextCff = cffList.get(nextCffIndex);
            nextCffIndex++;
        }

        return nextCff;
    }

    private boolean personDataIsNotInitialized() {
        return this.cffList == null;
    }

    private List<Cff> fetchCffDataFromApi() {
        ResponseEntity<Cff[]> response = restTemplate.getForEntity(
                url,
                Cff[].class
        );
        Cff[] cffData = response.getBody();
        return Arrays.asList(cffData);
    }

}
