package com.gdn.mapper;

import com.gdn.entity.Cff;
import com.gdn.entity.Merchant;
import com.gdn.entity.User;
import com.gdn.entity.Warehouse;
import com.gdn.request.CffRequest;

public class CffMapper {
    public static Cff toCff(CffRequest cffRequest){
        return Cff.builder()
                .id(cffRequest.getId())
                .tp(User.builder()
                        .id(cffRequest.getTp().getId())
                        .build())
                .merchant(Merchant.builder()
                        .name(cffRequest.getMerchant().getName())
                        .emailAddress(cffRequest.getMerchant().getEmailAddress())
                        .phoneNumber(cffRequest.getMerchant().getPhoneNumber())
                        .build())
                .warehouse(Warehouse.builder()
                        .id(cffRequest.getWarehouse().getId())
                        .build())
                .cffGoodList(CffGoodMapper.toCffGoodList(cffRequest.getCffGoodList()))
                .pickupPoint(PickupPointMapper.toPickupPoint(cffRequest.getPickupPoint()))
                .build();
    }
}
