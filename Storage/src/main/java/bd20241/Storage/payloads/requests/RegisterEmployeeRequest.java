package bd20241.Storage.payloads.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEmployeeRequest {
    private String cpf;
    private String name;
    private String email;
    private String phone;
    private String state;
    private String city;
    private String zip;
    private String street;
    private String number;
    private String complement;
    private String password;
}
