package com.gdn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gdn.SchedulingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    // TODO : Tambahkan annotation @CreationTimestamp???
    @Column(name = "uploaded_date")
    private Date uploadedDate;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "cff")
    @JsonManagedReference
    private List<CffGood> cffGoodList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "pickup_point_id")
    private PickupPoint pickupPoint;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tp_id")
    private User tp;

    // Warehouse dan User tidak diberikan parameter cascade.
    // Alasannya adalah karena User dan Warehouse adalah master data.
    // Artinya sudah pasti ada di DB sebelum upload CFF.
    // Kalau diberikan cascade (katakanlah PERSIST), maka saat upload CFF, record di table Warehouse dan User
    // akan di-override oleh value yang di upload CFF --> THIS IS BAD!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "scheduling_status")
    @Enumerated(value = EnumType.STRING)
    private SchedulingStatus schedulingStatus;

}
