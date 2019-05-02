package com.gdn.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "logistic_vendor_fleet")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticVendorFleet {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logistic_vendor_id")
    private LogisticVendor logisticVendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

}
