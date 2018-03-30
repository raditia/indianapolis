package com.gdn.warehouse.implementation;

import com.gdn.entity.Warehouse;
import com.gdn.repository.WarehouseRepository;
import com.gdn.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService{

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

}
