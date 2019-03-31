package com.gdn;

import com.gdn.laff.Box;
import com.gdn.laff.BoxItem;
import com.gdn.laff.Container;
import com.gdn.laff.LargestAreaFitFirstPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BajinganService {

    private static final int maxContainers = 1000;  // maximum number of containers which can be used
    // limit search using 5 seconds deadline
    private static final double deadline = System.currentTimeMillis() + 500000;

    @Autowired
    private LargestAreaFitFirstPackager largestAreaFitFirstPackager;

    public void executeBajingan() {
        List<Container> containers = new ArrayList<Container>();
        List<BoxItem> products = new ArrayList<BoxItem>();
        List<String> fleetList = new ArrayList<>();
        List<String> skuList = new ArrayList<>();
        containers.add(new Container("Van",16000, 16000, 10800, 30000));
        containers.add(new Container("Engkel",1600,3500,1600,51000));

        products.add(new BoxItem(new Box("ACER14WHT", 2, 3, 2, 2), 2));
//        products.add(new BoxItem(new Box("ASUS", 36, 25, 2, 24), 70));
//        products.add(new BoxItem(new Box("MCB", 20, 28, 2, 1), 100));
//        products.add(new BoxItem(new Box("MBP13", 23, 35, 3, 2), 27));
//        products.add(new BoxItem(new Box("MBP15", 25, 40, 3, 2), 27));
//        products.add(new BoxItem(new Box("MCB13", 20, 30, 1, 1), 100));

        largestAreaFitFirstPackager.setShits(containers, true, true);
        // match multiple containers
        List<Container> fits = largestAreaFitFirstPackager.packList(products, maxContainers,deadline);
//        Container fits = largestAreaFitFirstPackager.pack(products, deadline);

        System.out.println("Hasil: \n" + fits);
    }
}
