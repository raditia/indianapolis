package com.gdn.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CffRequest {

    private String id;

    private TradePartnershipRequest tp;

    private MerchantRequest merchant;

    private WarehouseRequest warehouse;

    private List<CffGoodRequest> cffGoodList;

    private PickupPointRequest pickupPoint;

}
