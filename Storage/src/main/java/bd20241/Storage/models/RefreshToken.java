package bd20241.Storage.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private long id;
    private String employeeCpf;
    private String refreshToken;
    private Instant expiryDate;
}
