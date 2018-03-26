package com.gdn.upload_cff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCffMerchant {

    private String name;

    private String emailAddress;

    private String phoneNumber;

}
