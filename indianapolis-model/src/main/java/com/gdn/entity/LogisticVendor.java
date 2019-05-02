package com.gdn.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "logistic_vendor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticVendor {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "logisticVendor")
    private List<LogisticVendorFleet> logisticVendorFleetList;

}
