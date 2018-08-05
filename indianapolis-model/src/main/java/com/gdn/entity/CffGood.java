package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cff_good")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CffGood {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "product")
    private String sku;

    @Column(name = "length")
    private Float length;

    @Column(name = "width")
    private Float width;

    @Column(name = "height")
    private Float height;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "cbm")
    private Float cbm;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cff_id")
    @JsonBackReference
    private Cff cff;

}
