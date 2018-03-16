package com.gdn.batch;

import com.gdn.cff.good.CffGoodService;
import com.gdn.entity.Category;
import com.gdn.entity.Cff;
import com.gdn.cff.CffService;
import com.gdn.entity.CffGood;
import com.gdn.header.cff.HeaderCffService;
import com.gdn.upload_cff.UploadCffResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JpaCffWriter implements ItemWriter<UploadCffResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaCffWriter.class);

    @Autowired
    private HeaderCffService headerCffService;
    @Autowired
    private CffService cffService;
    @Autowired
    private CffGoodService cffGoodService;


    @Override
    public void write(List<? extends UploadCffResponse> list) throws Exception {
        for (UploadCffResponse response:list
             ) {
            LOGGER.info("Writing header cff to database...\n" +
                    "ID : " + response.getRequestor().getId() + "\n" +
                    "Requestor name : " + response.getRequestor().getName() + "\n" +
                    "Date uploaded : " + response.getRequestor().getDate() + "\n");
            headerCffService.save(response.getRequestor());

            LOGGER.info("Writing cff to database...\n" +
                    "Category ID : " + response.getCategory() + "\n" +
                    "Warehouse ID : " + response.getWarehouse() + "\n" +
                    "Header CFF ID : " + response.getRequestor().getId() + "\n");
            Category category = Category.builder()
                    .id(response.getCategory())
                    .build();
            Cff cff = Cff.builder()
                    .id(response.getRequestor().getId())
                    .headerCff(response.getRequestor())
                    .category(category)
                    .build();
            cffService.saveCff(cff);
            for (CffGood good:response.getGoods()
                 ) {
                LOGGER.info("writing cff goods to database...\n" +
                        "CFF ID : " + response.getRequestor().getId() + "\n" +
                        "SKU : " + good.getSku() + "\n" +
                        "CBM : " + good.getCbm() + "\n" +
                        "Quantity : " + good.getQuantity() + "\n");
                CffGood cffGood = CffGood.builder()
                        .goods_id(good.getGoods_id())
                        .cff(cff)
                        .sku(good.getSku())
                        .cbm(good.getCbm())
                        .quantity(good.getQuantity())
                        .build();
                cffGoodService.save(cffGood);
            }
        }
    }

}
