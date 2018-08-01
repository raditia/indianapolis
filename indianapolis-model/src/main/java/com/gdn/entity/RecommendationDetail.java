package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_fleet_id")
    @JsonBackReference
    private RecommendationFleet recommendationFleet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cff_good")
    private CffGood cffGood;

    @Column(name = "sku_pickup_qty")
    private int skuPickupQty;

    @Column(name = "cbm_pickup_amount")
    private Float cbmPickupAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_point_id")
    private PickupPoint pickupPoint;

}
