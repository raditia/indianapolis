package com.gdn.upload_cff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCffPickupPoint {

    private String pickupAddress;
    private Double latitude;
    private Double longitude;

}
