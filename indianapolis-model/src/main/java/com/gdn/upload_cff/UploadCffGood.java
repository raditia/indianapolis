package com.gdn.upload_cff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCffGood {

    private String sku;
    private double cbm;
    private int quantity;

}
