package batch;

import dummymodel.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LogPersonProcessor implements ItemProcessor<Person, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPersonProcessor.class);

    @Override
    public String process(Person person) throws Exception {
        LOGGER.info("Now processing " + person.getName());
        return person.getName() + " berumur " + person.getAge() + " tahun";
    }

}
