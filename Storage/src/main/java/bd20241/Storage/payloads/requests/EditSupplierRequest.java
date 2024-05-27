package bd20241.Storage.payloads.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditSupplierRequest {
    private String name;
    private String email;
    private String phone;
    
}
