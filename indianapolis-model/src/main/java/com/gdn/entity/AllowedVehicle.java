package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_point_id")
    @JsonBackReference
    private PickupPoint pickupPoint;

}
