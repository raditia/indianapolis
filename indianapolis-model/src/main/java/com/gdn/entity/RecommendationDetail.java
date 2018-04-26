package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "recommendation_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDetail {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "recommendation_fleet_id")
    private RecommendationFleet recommendationFleet;

    @Column(name = "sku")
    private String sku;

    @Column(name = "sku_pickup_qty")
    private int skuPickupQty;

    @Column(name = "cbm_pickup_amount")
    private double cbmPickupAmount;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

}
