package com.gdn.cff.good;

import com.gdn.entity.CffGood;

import java.util.List;

public interface CffGoodService {
    CffGood save(CffGood cffGood);
    List<CffGood> findAll();
}
