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
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "sku_pickup")
    private int skuPickup;

    @Column(name = "cbm_pickup")
    private double cbmPickup;

}
