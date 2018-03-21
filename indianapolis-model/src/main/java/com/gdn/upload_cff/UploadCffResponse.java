package com.gdn.upload_cff;

import com.gdn.entity.CffGood;
import com.gdn.entity.HeaderCff;
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
    private String category;
    private String warehouse;
    private List<UploadCffGood> goods;

}
