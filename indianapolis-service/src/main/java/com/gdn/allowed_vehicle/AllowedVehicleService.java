package com.gdn.allowed_vehicle;

import com.gdn.entity.PickupPoint;
import com.gdn.upload_cff.UploadCffResponse;

public interface AllowedVehicleService {
    void save(PickupPoint pickupPoint, UploadCffResponse uploadCffResponse);
}
