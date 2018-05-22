package com.gdn.merchant;

import com.gdn.entity.Merchant;
import com.gdn.response.MerchantResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface MerchantService {
    WebResponse<List<MerchantResponse>> getAllMerchant();
    Merchant getOne(String email);
}
