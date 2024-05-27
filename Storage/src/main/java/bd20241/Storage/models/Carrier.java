package bd20241.Storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrier {
    private String cnpj;
    private String name;
    private String phone;
    private String email;
}
