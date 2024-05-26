package bd20241.Storage.payloads.requests;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private float weight;
    private float volume;
    private List<String> categories;
}
