package com.gdn.batch.upload_cff;

import com.gdn.upload_cff.UploadCffResponse;
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

public class JsonCffReader implements ItemReader<UploadCffResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonCffReader.class);

    private List<UploadCffResponse> cffList;
    private int nextCffIndex;
    private String url;
    private RestTemplate restTemplate;

    public JsonCffReader(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
        nextCffIndex = 0;
    }

    @Override
    public UploadCffResponse read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (personDataIsNotInitialized()) {
            cffList = fetchCffDataFromApi();
        }

        UploadCffResponse nextCff = null;

        if (nextCffIndex < cffList.size()) {
            nextCff = cffList.get(nextCffIndex);
            nextCffIndex++;
        }

        return nextCff;
    }

    private boolean personDataIsNotInitialized() {
        return this.cffList == null;
    }

    private List<UploadCffResponse> fetchCffDataFromApi() {
        ResponseEntity<UploadCffResponse[]> response = restTemplate.getForEntity(
                url,
                UploadCffResponse[].class
        );
        UploadCffResponse[] cffData = response.getBody();
        return Arrays.asList(cffData);
    }

}
