package com.gdn.mapper;

import com.gdn.entity.Cff;
import com.gdn.entity.CffGood;
import com.gdn.response.CffResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CffResponseMapper {

    public static CffResponse toCffResponse(Cff cff){
        Float cbmTotal = 0.0f;
        for (CffGood cffGood:cff.getCffGoodList()
             ) {
            cbmTotal+=cffGood.getCbm()*cffGood.getQuantity();
        }
        return CffResponse.builder()
                .pickupPointAddress(cff.getPickupPoint().getPickupAddress())
                .merchantName(cff.getMerchant().getName())
                .cffId(cff.getId())
                .cffGoodList(cff.getCffGoodList())
                .cbmTotal(cbmTotal)
                .pickupDate(cff.getPickupDate())
                .warehouseName(cff.getWarehouse().getAddress())
                .schedulingStatus(cff.getSchedulingStatus())
                .build();
    }

    public static List<CffResponse> toCffListResponse(List<Cff> cffList){
        return cffList.stream()
                .map(CffResponseMapper::toCffResponse)
                .collect(Collectors.toList());
    }

}
