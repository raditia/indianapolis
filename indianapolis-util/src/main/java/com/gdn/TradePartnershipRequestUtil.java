package com.gdn;

import com.gdn.request.TradePartnershipRequest;

public class TradePartnershipRequestUtil {
    public static TradePartnershipRequest tradePartnershipRequestCompleteAttribute = TradePartnershipRequest.builder()
            .id(UserUtil.userCompleteAttribute.getId())
            .build();
}
