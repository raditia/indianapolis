package com.gdn.util;

import com.gdn.entity.Merchant;

public class MerchantUtil {
    public static Merchant merchantCompleteAttribute = Merchant.builder()
            .id("1")
            .name("merchant")
            .emailAddress("email merchant 1")
            .phoneNumber("telp merchant 1")
            .build();
    public static Merchant newMerchantUploadCff = Merchant.builder()
            .name("merchant baru")
            .emailAddress("email merchant baru")
            .phoneNumber("telp merchant baru")
            .build();
    public static Merchant existingMerchantUploadCff = Merchant.builder()
            .name("merchant")
            .emailAddress("email merchant 1")
            .phoneNumber("telp merchant 1")
            .build();
}
