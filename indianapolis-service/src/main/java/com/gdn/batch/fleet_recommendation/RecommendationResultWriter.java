package com.gdn.batch.fleet_recommendation;

import com.gdn.Status;
import com.gdn.recommendation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecommendationResultWriter implements ItemWriter<List<Recommendation>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationResultWriter.class);

    @Override
    public void write(List<? extends List<Recommendation>> items) throws Exception {
        for (List<Recommendation> recommendationList:items
             ) {
            for (Recommendation recommendation:recommendationList
                 ) {
                System.out.println("\n" + "HASIL REKOMENDASI!");
                System.out.println("ID : " + recommendation.getId() + "\n" +
                        "TOTAL SKU : " + recommendation.getSkuAmount() + "\n" +
                        "TOTAL CBM : " + recommendation.getCbmTotal() + "\n" +
                        "DATE PICKUP : " + tomorrow() + "\n" +
                        "STATUS : " + Status.PENDING);
                for (Pickup pickup:recommendation.getPickupList()
                     ) {
                    System.out.println("\n" +
                            "FLEET : " + pickup.getFleet().getName() + "\n" +
                            "FLEET SKU PICKUP : " + pickup.getPickupTotalAmount() + "\n" +
                            "FLEET CBM PICKUP : " + pickup.getPickupTotalCbm());
                    for (Detail detail:pickup.getDetailList()
                         ) {
                        System.out.println("SKU DETAIL : " + detail.getSku().getName() + "\n" +
                                "SKU PICKUP : " + detail.getPickupAmount() + "\n" +
                                "CBM PICKUP : " + detail.getCbmPickup() + "\n" +
                                "WAREHOUSE TUJUAN : " + detail.getSku().getWarehouseId() + "\n" +
                                "MERCHANT ID : " + detail.getSku().getMerchantId());
                    }
                }
            }
        }
    }

    private String tomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(tomorrow);
    }

}
