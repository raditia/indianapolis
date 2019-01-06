package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    // Field PickupPoint ini tidak diberikan parameter cascade.
    // Alasannya adalah karena pada entity PickupPoint, kita sudah set untuk AllowedVehicle.
    // Kalau disini dikasih cascade (katakanlah PERSIST), maka nanti di table PickupPoint akan menjadi null.
    // Karena kita gak secara explicit nge-set field PickupPoint di AllowedVehicle.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_point_id")
    @JsonBackReference
    private PickupPoint pickupPoint;

}
