package bd20241.Storage.payloads.requests;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String clientId;
    private String employeeCpf;
    private String deliveryAddressId;
    private String carrierCnpj;
    private ArrayList<String> itemIds;
}
