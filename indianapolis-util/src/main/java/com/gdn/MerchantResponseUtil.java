package com.gdn;

import com.gdn.response.MerchantResponse;

import java.util.ArrayList;
import java.util.List;

public class MerchantResponseUtil {
    public static MerchantResponse merchantResponseCompleteAttribute = MerchantResponse.builder()
            .name(MerchantUtil.merchantCompleteAttribute.getName())
            .emailAddress(MerchantUtil.merchantCompleteAttribute.getEmailAddress())
            .phoneNumber(MerchantUtil.merchantCompleteAttribute.getPhoneNumber())
            .build();
    public static List<MerchantResponse> merchantResponseListCompleteAttribute = new ArrayList<MerchantResponse>(){{
        add(merchantResponseCompleteAttribute);
    }};
}
