package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    private Double price;

    @Column(name = "cbm_capacity")
    private Float cbmCapacity;

    @Column(name = "min_cbm_capacity")
    private Float minCbm;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "fleet")
    private List<LogisticVendorFleet> logisticVendorFleetList;

}
