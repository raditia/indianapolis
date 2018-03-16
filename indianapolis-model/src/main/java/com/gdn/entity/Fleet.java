package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "fleet")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fleet {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "cbm_capacity")
    private Float cbmCapacity;

    @ManyToOne
    @JoinColumn(name = "logisctic_vendor_id")
    private LogisticVendor logisticVendor;

}
