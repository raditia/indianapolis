package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    @Column(name = "fleet_plate_number")
    private String plateNumber;

}
