package bd20241.Storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Package {
    private String id;
    private String trackingCode;
    private String deliveryNotes;
    private String orderId;
    private boolean fragile;
}
