package com.gdn.mapper;

import com.gdn.entity.LogisticVendor;
import com.gdn.entity.LogisticVendorFleet;

import java.util.List;
import java.util.stream.Collectors;

public class LogisticVendorMapper {
    public static LogisticVendor toLogisticVendor(LogisticVendorFleet logisticVendorFleet){
        return LogisticVendor.builder()
                .id(logisticVendorFleet.getLogisticVendor().getId())
                .name(logisticVendorFleet.getLogisticVendor().getName())
                .build();
    }
    public static List<LogisticVendor> toLogisticVendorList(List<LogisticVendorFleet> logisticVendorFleetList){
        return logisticVendorFleetList.stream()
                .map(LogisticVendorMapper::toLogisticVendor)
                .collect(Collectors.toList());
    }
}
