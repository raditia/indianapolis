package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "cff_good")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CffGood {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long goods_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cff_id")
    private Cff cff;

    @Column(name = "sku")
    private String sku;

    @Column(name = "cbm")
    private double cbm;

    @Column(name = "quantity")
    private int quantity;

}
