package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(name = "pickup_address", unique = true)
    private String pickupAddress;

    @Column(name = "latitude", unique = true)
    private Double latitude;

    @Column(name = "longitude", unique = true)
    private Double longitude;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_point_id")
    @Builder.Default
    @JsonManagedReference
    private List<AllowedVehicle> allowedVehicleList = new ArrayList<>();

}
