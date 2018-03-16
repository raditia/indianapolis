package com.gdn.entity;

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
    private String goods_id;

    @ManyToOne
    @JoinColumn(name = "cff_id")
    private Cff cff;

    @Column(name = "sku")
    private String sku;

    @Column(name = "cbm")
    private double cbm;

    @Column(name = "quantity")
    private int quantity;

}
