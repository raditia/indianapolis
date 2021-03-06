package com.gdn.warehouse.implementation;

import com.gdn.repository.RecommendationResultRepository;
import com.gdn.repository.WarehouseRepository;
import com.gdn.response.WarehouseResponse;
import com.gdn.response.WebResponse;
import com.gdn.warehouse.WarehouseService;
import com.gdn.mapper.WarehouseResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private RecommendationResultRepository recommendationResultRepository;

    @Override
    public WebResponse<List<WarehouseResponse>> findAllWarehouse() {
        return WebResponse.OK(WarehouseResponseMapper.toWarehouseResponseList(warehouseRepository.findAll()));
    }

    @Override
    public WebResponse<List<WarehouseResponse>> findDistinctAllWarehouseInRecommendationResult() {
        return WebResponse.OK(WarehouseResponseMapper.toWarehouseResponseList(recommendationResultRepository.findDistinctAllWarehouse()));
    }

}
