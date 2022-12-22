package Models.Configs;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "loadData")
public class LoadConfig {
    @XmlElement(name = "loadFactor")
    private double loadFactor;
    @XmlElementWrapper(name = "loads")
    @XmlElement(name = "load")
    private List<Double> loads;
    public double getLoadAtHour(int hour) {
        return loads.get(hour) * loadFactor;
    }
}
