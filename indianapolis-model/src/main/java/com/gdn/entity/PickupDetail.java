package com.gdn.entity;

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

    @ManyToOne
    @JoinColumn(name = "pickup_id")
    private Pickup pickup;

    @ManyToOne
    @JoinColumn(name = "sku_id")
    private CffGood sku;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "pickup_point_id")
    private PickupPoint pickupPoint;

    @Column(name = "sku_pickup_quantity")
    private int skuPickupQuantity;

    @Column(name = "cbm_pickup_amount")
    private double cbmPickupAmount;

}
