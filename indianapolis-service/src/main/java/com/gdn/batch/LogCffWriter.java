package com.gdn.batch;

import com.gdn.Cff;
import com.gdn.cff.CffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LogCffWriter implements ItemWriter<Cff> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogCffWriter.class);

    @Autowired
    private CffService cffService;

    @Override
    public void write(List<? extends Cff> list) throws Exception {
        for (Cff cff:list
             ) {
                cffService.saveCff(cff);
            LOGGER.info("\nID : " + cff.getId() + "\n" +
                    "SKU : " + cff.getSku() + "\n" +
                    "Product name : " + cff.getProductName() + "\n" +
                    "CBM : " + cff.getCbm() + " CBM\n" +
                    "Quantity : " + cff.getQuantity() + " pc(s)\n" +
                    "Category : " + cff.getCategory().getName() + "\n" +
                    "Date uploaded : " + cff.getHeaderCff().getDateUploaded() + "\n" +
                    "Uploaded by : " + cff.getHeaderCff().getTpName() + "\n");
        }
    }

}
