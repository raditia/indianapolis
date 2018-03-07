package com.gdn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "allowed_vehicle")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllowedVehicle {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "vehicle_name")
    private String vehicleName;

    @ManyToOne
    @JoinColumn(name = "pickup_point_id")
    private PickupPoint pickupPoint;

}
