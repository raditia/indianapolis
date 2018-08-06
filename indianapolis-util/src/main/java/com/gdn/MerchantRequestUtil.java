package com.gdn;

import com.gdn.request.MerchantRequest;

public class MerchantRequestUtil {
    public static MerchantRequest existingMerchantRequestCompleteAttribute = MerchantRequest.builder()
            .name(MerchantUtil.merchantCompleteAttribute.getName())
            .emailAddress(MerchantUtil.merchantCompleteAttribute.getEmailAddress())
            .phoneNumber(MerchantUtil.merchantCompleteAttribute.getPhoneNumber())
            .build();
    public static MerchantRequest newMerchantRequestCompleteAttribute = MerchantRequest.builder()
            .name("new merchant name")
            .emailAddress("new merchant email")
            .phoneNumber("new merchant phone")
            .build();
}
