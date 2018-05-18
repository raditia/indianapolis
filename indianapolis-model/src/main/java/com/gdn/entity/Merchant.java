package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "merchant")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "email_address", unique = true)
    private String emailAddress;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

}
