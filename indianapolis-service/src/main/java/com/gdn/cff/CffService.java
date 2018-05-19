package com.gdn.cff;

import com.gdn.entity.Cff;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface CffService {
    WebResponse<List<CffResponse>> getAllCff();
    WebResponse<CffResponse> saveCff(Cff cff);
    Cff updateSchedulingStatus(String id);
}
