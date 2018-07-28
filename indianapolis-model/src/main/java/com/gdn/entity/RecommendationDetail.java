package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gdn.recommendation.Sku;
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
    @JsonBackReference
    private RecommendationFleet recommendationFleet;

    @ManyToOne
    @JoinColumn(name = "sku")
    private CffGood sku;

    @Column(name = "sku_pickup_qty")
    private int skuPickupQty;

    @Column(name = "cbm_pickup_amount")
    private Float cbmPickupAmount;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "pickup_point_id")
    private PickupPoint pickupPoint;

}
