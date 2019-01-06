package com.gdn.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogisticVendorResponse {
    private String id;
    private String name;
}
