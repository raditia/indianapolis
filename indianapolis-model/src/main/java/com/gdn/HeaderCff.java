package com.gdn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "header_cff")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeaderCff {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "date_uploaded")
    private Date dateUploaded;

    @Column(name = "tp_name")
    private String tpName;

}
