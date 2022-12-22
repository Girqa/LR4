package Models.Consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerRequest {
    @NonNull
    private double energy;
    @NonNull
    private double maxPrice;
}
