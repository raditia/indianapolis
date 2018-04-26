package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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

    @Column(name = "total_sku")
    private int totalSku;

    @Column(name = "total_cbm")
    private double totalCbm;

    @Column(name = "pickup_date")
    private Date pickupDate;

}
