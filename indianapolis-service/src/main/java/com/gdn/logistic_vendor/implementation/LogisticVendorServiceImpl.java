package com.gdn.logistic_vendor.implementation;

import com.gdn.logistic_vendor.LogisticVendorService;
import com.gdn.mapper.LogisticVendorResponseMapper;
import com.gdn.repository.LogisticVendorRepository;
import com.gdn.response.LogisticVendorResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticVendorServiceImpl implements LogisticVendorService {

    @Autowired
    private LogisticVendorRepository logisticVendorRepository;

    @Override
    public WebResponse<List<LogisticVendorResponse>> findAll() {
        return WebResponse.OK(LogisticVendorResponseMapper.toLogisticVendorResponseList(logisticVendorRepository.findAll()));
    }
}
