package bd20241.Storage.payloads.responses;

import java.util.List;

import bd20241.Storage.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends Product {
    private List<String> categories;
}
