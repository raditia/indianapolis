package com.gdn.cff;

import com.gdn.entity.Cff;
import com.gdn.request.CffRequest;
import com.gdn.response.CffResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface CffService {
    WebResponse<List<CffResponse>> getAllCff();
    WebResponse<CffResponse> getOneCff(String cffId);
    WebResponse<CffResponse> saveCff(CffRequest cffRequest);
    Cff updateSchedulingStatus(String id);
}
