package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "address")
    private String pickupAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "pickupPoint")
    @JsonManagedReference
    private List<AllowedVehicle> allowedVehicleList;

}
