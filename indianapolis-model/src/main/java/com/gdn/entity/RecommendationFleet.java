package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "recommendation_fleet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationFleet {

    @Id
    @Column(name = "recommendation_fleet")
    private String id;

    @ManyToOne
    @JoinColumn(name = "recommendation_result_id")
    private RecommendationResult recommendationResult;

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    @Column(name = "fleet_sku_pickup_qty")
    private int fleetSkuPickupQty;

    @Column(name = "fleet_cbm_pickup_amount")
    private double fleetCbmPickupAmount;

}
