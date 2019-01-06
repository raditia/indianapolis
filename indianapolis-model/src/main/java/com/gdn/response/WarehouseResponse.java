package com.gdn.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseResponse {

    private String id;

    private String address;

}
