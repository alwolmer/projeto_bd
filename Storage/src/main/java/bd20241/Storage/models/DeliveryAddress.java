package bd20241.Storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress {
    private String id;
    private String recipientName;
    private String state;
    private String city;
    private String zip;
    private String street;
    private String number;
    private String details;
    private String clientId;
}
