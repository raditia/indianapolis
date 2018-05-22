package com.gdn.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantResponse {

    private String name;

    private String emailAddress;

    private String phoneNumber;

}
