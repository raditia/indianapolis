package com.gdn.warehouse.implementation;

import com.gdn.entity.Warehouse;
import com.gdn.repository.WarehouseRepository;
import com.gdn.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    WarehouseRepository warehouseRepository;

    @Override
    public void addDefaultWarehouseInformation() {
        Warehouse warehouseCawang = Warehouse.builder()
                .id("warehouse_cawang")
                .address("Alamat warehouse cawang")
                .phoneNumber("0123456879")
                .emailAddress("cawang@gdn-warehouse.com")
                .build();
        Warehouse warehouseCakung = Warehouse.builder()
                .id("warehouse_cakung")
                .address("Alamat warehouse cakung")
                .phoneNumber("0123456879")
                .emailAddress("cakung@gdn-warehouse.com")
                .build();
        Warehouse warehouseCeper = Warehouse.builder()
                .id("warehouse_ceper")
                .address("Alamat warehouse ceper")
                .phoneNumber("0123456879")
                .emailAddress("ceper@gdn-warehouse.com")
                .build();
        warehouseRepository.save(warehouseCawang); warehouseRepository.save(warehouseCakung); warehouseRepository.save(warehouseCeper);
    }

}
