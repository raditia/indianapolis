package com.gdn.upload_cff;

import com.gdn.entity.CffGood;
import com.gdn.entity.HeaderCff;
import com.gdn.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCffResponse {

    private HeaderCff requestor;
    private UploadCffMerchant merchant;
    private UploadCffPickupPoint pickupPoint;
    private List<UploadCffAllowedVehicle> allowedVehicles;
    private String category;
    private String warehouse;
    private List<UploadCffGood> goods;

}
