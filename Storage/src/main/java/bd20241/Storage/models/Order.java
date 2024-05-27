package bd20241.Storage.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id;
    private String clientId;
    private String employeeCpf;
    private String deliveryAddressId;
    private String carrierCnpj;
    private Date createdAt;
    private Date updatedAt;
}
