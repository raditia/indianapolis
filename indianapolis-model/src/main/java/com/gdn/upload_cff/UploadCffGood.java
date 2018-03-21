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

    @Column(name = "sku")
    private String sku;

    @Column(name = "cbm")
    private double cbm;

    @Column(name = "quantity")
    private int quantity;

}
