package com.gdn.mapper;

import com.gdn.entity.LogisticVendor;
import com.gdn.response.LogisticVendorResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LogisticVendorResponseMapper {
    public static LogisticVendorResponse toLogisticVendorResponse(LogisticVendor logisticVendor){
        return LogisticVendorResponse.builder()
                .id(logisticVendor.getId())
                .name(logisticVendor.getName())
                .build();
    }
    public static List<LogisticVendorResponse> toLogisticVendorResponseList(List<LogisticVendor> logisticVendorList){
        return logisticVendorList.stream()
                .map(LogisticVendorResponseMapper::toLogisticVendorResponse)
                .collect(Collectors.toList());
    }
}
