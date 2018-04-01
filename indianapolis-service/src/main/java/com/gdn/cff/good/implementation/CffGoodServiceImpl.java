package com.gdn.cff.good.implementation;

import com.gdn.cff.good.CffGoodService;
import com.gdn.entity.Cff;
import com.gdn.entity.CffGood;
import com.gdn.repository.CffGoodRepository;
import com.gdn.upload_cff.UploadCffGood;
import com.gdn.upload_cff.UploadCffResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CffGoodServiceImpl implements CffGoodService {

    @Autowired
    CffGoodRepository cffGoodRepository;

    @Override
    public void save(Cff cff, UploadCffResponse response) {
        for (UploadCffGood good:response.getGoods()
                ) {
            cffGoodRepository.save(buildCffGoods(cff, good));
        }
    }


    private CffGood buildCffGoods(Cff cff,
                                  UploadCffGood good){
        return CffGood.builder()
                .id(UUID.randomUUID().toString())
                .cff(cff)
                .sku(good.getSku())
                .cbm(good.getCbm())
                .quantity(good.getQuantity())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CffGood> findAll() {
        return cffGoodRepository.findAll();
    }

}
