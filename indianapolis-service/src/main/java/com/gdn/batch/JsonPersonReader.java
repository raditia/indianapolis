package com.gdn.batch;

import com.gdn.dummymodel.Person;
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

public class JsonPersonReader implements ItemReader<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonPersonReader.class);

    private List<Person> personList;
    private int nextPersonIndex;
    private String url;
    private RestTemplate restTemplate;

    public JsonPersonReader(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
        nextPersonIndex = 0;
    }

    @Override
    public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (personDataIsNotInitialized()) {
            personList = fetchPersonDataFromApi();
        }

        Person nextPerson = null;

        if (nextPersonIndex < personList.size()) {
            nextPerson = personList.get(nextPersonIndex);
            nextPersonIndex++;
        }

        return nextPerson;
    }

    private boolean personDataIsNotInitialized() {
        return this.personList == null;
    }

    private List<Person> fetchPersonDataFromApi() {
        ResponseEntity<Person[]> response = restTemplate.getForEntity(
                url,
                Person[].class
        );
        Person[] studentData = response.getBody();
        return Arrays.asList(studentData);
    }

}
