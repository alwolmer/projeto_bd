package bd20241.Storage.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discard {
    private String employeeCpf;
    private String itemId;
    private String discardReason;
    private Date createdAt;
    private Date updatedAt;    
}
