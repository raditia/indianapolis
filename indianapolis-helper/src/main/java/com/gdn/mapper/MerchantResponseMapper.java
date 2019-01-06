package com.gdn.mapper;

import com.gdn.entity.Merchant;
import com.gdn.response.MerchantResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MerchantResponseMapper {

    public static MerchantResponse toMerchantResponse(Merchant merchant){
        return MerchantResponse.builder()
                .name(merchant.getName())
                .emailAddress(merchant.getEmailAddress())
                .phoneNumber(merchant.getPhoneNumber())
                .build();
    }

    public static List<MerchantResponse> toMerchantResponseList(List<Merchant> merchantList){
        return merchantList.stream()
                .map(MerchantResponseMapper::toMerchantResponse)
                .collect(Collectors.toList());
    }

}
