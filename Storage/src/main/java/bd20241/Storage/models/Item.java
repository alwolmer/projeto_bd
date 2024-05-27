package bd20241.Storage.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {
    private String id;
    private String productId;
    private String supplierCnpj;
    private String employeeCpf;
    private Date createdAt;
    private Date updatedAt;
}
