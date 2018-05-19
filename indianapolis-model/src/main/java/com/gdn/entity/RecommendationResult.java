package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recommendation_result")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResult {

    @Id
    @Column(name = "id")
    private String id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recommendation_result_id")
    @Builder.Default
    @JsonManagedReference
    private List<RecommendationFleet> recommendationFleetList = new ArrayList<>();

    @Column(name = "total_sku")
    private int totalSku;

    @Column(name = "total_cbm")
    private double totalCbm;

    @Column(name = "pickup_date")
    private Date pickupDate;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

}
