package com.gdn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "header_cff_id")
    private HeaderCff headerCff;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
