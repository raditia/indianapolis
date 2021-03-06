package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "role")
    private String role;

}
