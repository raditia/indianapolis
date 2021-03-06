package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pickup_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PickupDetail {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_fleet_id")
    @JsonBackReference
    private PickupFleet pickupFleet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cff_good_id")
    private CffGood cffGood;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_point_id")
    private PickupPoint pickupPoint;

    @Column(name = "cff_good_pickup_quantity")
    private int skuPickupQuantity;

    @Column(name = "cbm_pickup_amount")
    private Float cbmPickupAmount;

}
