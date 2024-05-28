package bd20241.Storage.payloads.responses;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockStatsResponse {
    private Date date;
    private int itemCount;
}
