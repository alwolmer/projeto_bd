package bd20241.Storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String cpf;
    private String cnpj;
}
