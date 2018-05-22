package mapper;

import com.gdn.entity.Warehouse;
import com.gdn.response.WarehouseResponse;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseResponseMapper {

    public static WarehouseResponse toWarehouseResponse(Warehouse warehouse){
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .address(warehouse.getAddress())
                .build();
    }

    public static List<WarehouseResponse> toWarehouseResponseList(List<Warehouse> warehouseList){
        return warehouseList.stream()
                .map(WarehouseResponseMapper::toWarehouseResponse)
                .collect(Collectors.toList());
    }

}
