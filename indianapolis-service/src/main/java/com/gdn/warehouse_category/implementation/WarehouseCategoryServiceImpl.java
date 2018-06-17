package com.gdn.warehouse_category.implementation;

import com.gdn.repository.WarehouseCategoryRepository;
import com.gdn.response.WarehouseCategoryResponse;
import com.gdn.response.WebResponse;
import com.gdn.warehouse_category.WarehouseCategoryService;
import com.gdn.mapper.WarehouseCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseCategoryServiceImpl implements WarehouseCategoryService {

    @Autowired
    private WarehouseCategoryRepository warehouseCategoryRepository;

    @Override
    public WebResponse<List<WarehouseCategoryResponse>> findAllWarehouseCategory() {
        return WebResponse.OK(WarehouseCategoryMapper.toWarehouseCategoryResponseList(warehouseCategoryRepository.findAll()));
    }

}
