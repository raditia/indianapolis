package com.gdn.pickup;

import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.WebResponse;

import javax.mail.MessagingException;

public interface PickupService {
    WebResponse<PickupChoiceResponse> savePickup(PickupChoiceRequest pickupChoiceRequest) throws MessagingException;
}
