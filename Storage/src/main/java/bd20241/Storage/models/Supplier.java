package bd20241.Storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    private String cnpj;
    private String name;
    private String email;
    private String phone;
}
