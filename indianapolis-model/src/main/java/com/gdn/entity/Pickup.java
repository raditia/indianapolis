package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pickup")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pickup {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "pickup_date")
    private Date pickupDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    @Column(name = "fleet_plate_number")
    private String plateNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "pickup_id")
    @JsonManagedReference
    private List<PickupDetail> pickupDetailList;

}
