package com.gdn.cff.good;

import com.gdn.entity.Cff;
import com.gdn.entity.CffGood;
import com.gdn.upload_cff.UploadCffResponse;

import java.util.List;

public interface CffGoodService {
    void save(Cff cff, UploadCffResponse response);
    List<CffGood> findAll();
}
