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

        containers.add(new Container("van",160, 150, 108, 1500));
        containers.add(new Container("pickup",155, 237, 108, 3000));
        containers.add(new Container("engkel",160,350,160,5100));
        containers.add(new Container("double",200, 560, 220, 8000));
        containers.add(new Container("fuso",230, 570, 240, 9000));
        containers.add(new Container("tronton",220, 630, 230, 15000));
        containers.add(new Container("build up",230, 650, 230, 20000));

//        products.add(new BoxItem(new Box("LG T2109VS2M Mesin Cuci Top Loading - Silver [Khusus Jadetabek]", 59,58,95,81), 125));
        products.add(new BoxItem(new Box("POLYTRON PLD 43D150 Xcel TV LED [43 Inch]",15,	106,	70,	10.9), 62));
        products.add(new BoxItem(new Box("Acer Predator XB271HU Monitor [27 Inch/ Res 2560x1440]",	30,	70,	40,	10), 62));
        products.add(new BoxItem(new Box("Canon PIXMA E560 Wifi Multifungsi Printer",	31,	45,	16,	9), 66));
        products.add(new BoxItem(new Box("Acer Swift 3 SF314-56G 57A7 Notebook - Biru [i5-8265U/ MX150 2G/ 4G/ 1TB/ 14 Inch/ Win10]",27,	37,	15,	4), 62));
        products.add(new BoxItem(new Box("Xiaomi Smart TV Soundbar Audio with 8 speakers Xiaomi Sound Bar TV",	10,	90,	15,	4), 62));
        products.add(new BoxItem(new Box("SONY WH-CH700N Wireless N.C Headset",	12,	25,	20,	1), 62));
        products.add(new BoxItem(new Box("Samsung Galaxy Note 9 Smartphone [512GB/ 8GB]",14,	21,	8,0.5), 62));

        largestAreaFitFirstPackager.setContainer(containers, true, true);
        // match multiple containers
        List<Container> fits = largestAreaFitFirstPackager.packList(products, maxContainers,deadline);
//        Container fits = largestAreaFitFirstPackager.pack(products, deadline);

        System.out.println("Hasil: \n" + fits);
    }
}
