package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recommendation_fleet_id")
    @Builder.Default
    @JsonManagedReference
    private List<RecommendationDetail> recommendationDetailList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recommendation_result_id")
    @JsonBackReference
    private RecommendationResult recommendationResult;

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    @Column(name = "fleet_sku_pickup_qty")
    private int fleetSkuPickupQty;

    @Column(name = "fleet_cbm_pickup_amount")
    private double fleetCbmPickupAmount;

}
