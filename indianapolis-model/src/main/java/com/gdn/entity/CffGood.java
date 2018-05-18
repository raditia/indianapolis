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

    @Column(name = "sku")
    private String sku;

    @Column(name = "length")
    private double length;

    @Column(name = "width")
    private double width;

    @Column(name = "height")
    private double height;

    @Column(name = "weight")
    private double weight;

    @Column(name = "cbm")
    private double cbm;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cff_id")
    @JsonBackReference
    private Cff cff;

}
