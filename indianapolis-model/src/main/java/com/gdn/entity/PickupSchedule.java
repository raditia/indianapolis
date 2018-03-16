package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pickup_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickupSchedule {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "pickup_date_time")
    private Date pickupDateTime;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;
}
