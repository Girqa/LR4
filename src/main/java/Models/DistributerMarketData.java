package Models;

import jade.core.AID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistributerMarketData {
    private String marketName;
    private AID consumer;
    private ConsumerRequest consumerRequest;
}
