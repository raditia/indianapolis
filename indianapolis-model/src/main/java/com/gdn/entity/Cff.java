package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gdn.SchedulingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cff")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cff {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "pickup_date")
    private Date pickupDate;

    @Column(name = "date_uploaded")
    private Date uploadedDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cff_id")
    @Builder.Default
    @JsonManagedReference
    private List<CffGood> cffGoodList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_point_id")
    private PickupPoint pickupPoint;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "cff_id")
//    @Builder.Default
//    private List<PickupPoint> pickupPointList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "tp_id")
    private User tp;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "status")
    private String schedulingStatus;

}
