package mapper;

import com.gdn.entity.Pickup;
import com.gdn.response.PickupChoiceResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PickupChoiceResponseMapper {

    public static PickupChoiceResponse toPickupChoiceResponse(Pickup pickup){
        return PickupChoiceResponse.builder()
                .pickupDate(pickup.getPickupDate())
                .fleetName(pickup.getFleet().getName())
                .fleetPlateNumber(pickup.getPlateNumber())
                .build();
    }

    public static List<PickupChoiceResponse> toPickupChoiceResponseList(List<Pickup> pickupList){
        return pickupList.stream()
                .map(PickupChoiceResponseMapper::toPickupChoiceResponse)
                .collect(Collectors.toList());
    }

}
