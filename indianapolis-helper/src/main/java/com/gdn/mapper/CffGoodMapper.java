package com.gdn.mapper;

import com.gdn.entity.CffGood;
import com.gdn.request.CffGoodRequest;

import java.util.List;
import java.util.stream.Collectors;

public class CffGoodMapper {
    public static CffGood toCffGood(CffGoodRequest cffGoodRequest){
        return CffGood.builder()
                .sku(cffGoodRequest.getSku())
                .length(cffGoodRequest.getLength())
                .width(cffGoodRequest.getWidth())
                .height(cffGoodRequest.getHeight())
                .weight(cffGoodRequest.getWeight())
                .cbm(cffGoodRequest.getCbm())
                .quantity(cffGoodRequest.getQuantity())
                .build();
    }
    public static List<CffGood> toCffGoodList(List<CffGoodRequest> cffGoodRequestList){
        return cffGoodRequestList.stream()
                .map(CffGoodMapper::toCffGood)
                .collect(Collectors.toList());
    }
}
