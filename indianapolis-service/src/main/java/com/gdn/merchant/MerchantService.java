package com.gdn.merchant;

import com.gdn.entity.Merchant;

public interface MerchantService {
    Merchant getOne(String email);
}
