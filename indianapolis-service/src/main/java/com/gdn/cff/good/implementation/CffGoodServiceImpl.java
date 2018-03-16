package com.gdn.cff.good.implementation;

import com.gdn.cff.good.CffGoodService;
import com.gdn.entity.CffGood;
import com.gdn.repository.CffGoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CffGoodServiceImpl implements CffGoodService {

    @Autowired
    CffGoodRepository cffGoodRepository;

    @Override
    public CffGood save(CffGood cffGood) {
        return cffGoodRepository.save(cffGood);
    }

    @Override
    public List<CffGood> findAll() {
        return cffGoodRepository.findAll();
    }

}
