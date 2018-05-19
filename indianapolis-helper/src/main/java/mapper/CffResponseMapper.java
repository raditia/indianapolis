package mapper;

import com.gdn.entity.Cff;
import com.gdn.entity.CffGood;
import com.gdn.response.CffResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class CffResponseMapper {

    public static CffResponse toCffResponse(Cff cff){
        double cbmTotal = 0;
        for (CffGood cffGood:cff.getCffGoodList()
             ) {
            cbmTotal+=cffGood.getCbm();
        }
        return CffResponse.builder()
                .pickupPointAddress(cff.getPickupPoint().getPickupAddress())
                .merchantName(cff.getMerchant().getName())
                .cffGoodList(cff.getCffGoodList())
                .cbmTotal(cbmTotal)
                .uploadedDate(cff.getUploadedDate())
                .warehouseName(cff.getWarehouse().getAddress())
                .schedulingStatus(cff.getSchedulingStatus())
                .build();
    }

    public static List<CffResponse> toCffListResponse(List<Cff> cffList){
        return cffList.stream()
                .map(CffResponseMapper::toCffResponse)
                .collect(Collectors.toList());
    }

}
