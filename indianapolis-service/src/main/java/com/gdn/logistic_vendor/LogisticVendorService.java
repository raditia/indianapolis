package com.gdn.logistic_vendor;

import com.gdn.response.LogisticVendorResponse;
import com.gdn.response.WebResponse;

import java.util.List;

public interface LogisticVendorService {
    WebResponse<List<LogisticVendorResponse>> findAll();
}
