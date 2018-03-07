import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pickup_point")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickupPoint {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "header_cff_id")
    private HeaderCff headerCff;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
}
