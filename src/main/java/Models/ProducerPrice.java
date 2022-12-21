package Models;

import Interfaces.Valuable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProducerPrice implements Valuable {
    private double price;
    private double volume;
    public Double getPrice(){
        return price;
    }
    public double getVolume() {
        return volume;
    }
}
