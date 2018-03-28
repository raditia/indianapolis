package com.gdn.merchant.implementation;

import com.gdn.entity.Merchant;
import com.gdn.merchant.MerchantService;
import com.gdn.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

}
