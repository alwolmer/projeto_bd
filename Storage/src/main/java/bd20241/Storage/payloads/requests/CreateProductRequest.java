package bd20241.Storage.payloads.requests;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private Date expirationDate;
}
