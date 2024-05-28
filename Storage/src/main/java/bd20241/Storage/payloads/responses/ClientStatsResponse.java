package bd20241.Storage.payloads.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientStatsResponse {
    private String clientType;
    private int clientCount;
}
