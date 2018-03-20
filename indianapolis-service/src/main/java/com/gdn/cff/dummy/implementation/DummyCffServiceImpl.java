package com.gdn.cff.dummy.implementation;

import com.gdn.cff.dummy.DummyCffService;
import com.gdn.entity.CffGood;
import com.gdn.entity.HeaderCff;
import com.gdn.upload_cff.UploadCffResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DummyCffServiceImpl implements DummyCffService {
    @Override
    public UploadCffResponse createOneDummyUploadCffResponse(UploadCffResponse uploadCffResponse) {
        return UploadCffResponse.builder()
                .requestor(uploadCffResponse.getRequestor())
                .category(uploadCffResponse.getCategory())
                .warehouse(uploadCffResponse.getWarehouse())
                .goods(uploadCffResponse.getGoods())
                .build();
    }

    @Override
    public List<UploadCffResponse> getDummyUploadCffResponse() {
        List<CffGood> cffGoodList = new ArrayList<>();
        CffGood camera1 = CffGood.builder()
                .goods_id(UUID.randomUUID().toString())
                .sku("Glenz GFDS-87508 Standalone 5MP DVR XMEYE - Black Black")
                .cbm(3)
                .quantity(2)
                .build();
        CffGood camera2 = CffGood.builder()
                .goods_id(UUID.randomUUID().toString())
                .sku("Glenz GFCA-29540 Indoor 5.0MP Camera AHD Sony Starvis - White White")
                .cbm(3)
                .quantity(5)
                .build();
        cffGoodList.add(camera1); cffGoodList.add(camera2);
        UploadCffResponse uploadCffResponse = createOneDummyUploadCffResponse(UploadCffResponse.builder()
                .requestor(HeaderCff.builder()
                        .id(UUID.randomUUID().toString())
                        .date(new Date())
                        .name("Komang")
                        .build())
                .category("category_camera")
                .warehouse("warehouse_cawang")
                .goods(cffGoodList)
                .build());
        List<UploadCffResponse> uploadCffResponseList = new ArrayList<>();
        uploadCffResponseList.add(uploadCffResponse);
        return uploadCffResponseList;
    }
}
