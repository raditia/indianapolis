package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "pickup_point")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickupPoint {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "header_cff_id")
    private HeaderCff headerCff;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
}
