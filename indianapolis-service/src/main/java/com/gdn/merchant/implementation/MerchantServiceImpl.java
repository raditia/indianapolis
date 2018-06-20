package com.gdn.merchant.implementation;

import com.gdn.entity.Merchant;
import com.gdn.merchant.MerchantService;
import com.gdn.repository.MerchantRepository;
import com.gdn.response.MerchantResponse;
import com.gdn.response.WebResponse;
import com.gdn.mapper.MerchantResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public WebResponse<List<MerchantResponse>> getAllMerchant() {
        return WebResponse.OK(MerchantResponseMapper.toMerchantResponseList(merchantRepository.findAll()));
    }

    @Override
    public Merchant getOne(String email) {
        return merchantRepository.findByEmailAddress(email);
    }

}
