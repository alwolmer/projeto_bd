package bd20241.Storage.payloads.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginEmployeeRequest {
    private String cpf;
    private String password;
}
