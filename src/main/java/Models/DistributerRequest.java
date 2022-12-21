package Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributerRequest {
    private ConsumerRequest consumerRequest;
    private String topicName;
}
