import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cff_pickup_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CffPickupSchedule {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "pickup_schedule_id")
    private PickupSchedule pickupSchedule;

    @ManyToOne
    @JoinColumn(name = "cff_id")
    private Cff cff;

}
