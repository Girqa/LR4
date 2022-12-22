package Models.Configs.Plants;

import Interfaces.Generation;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SolarPowerPlant")
public class SolarPowerPlant implements Generation {
    @XmlElementWrapper(name = "solarCoefficients")
    @XmlElement(name = "coef")
    private List<Double> solarCoefficients;
    @Override
    public Double apply(Integer t) {
        if (t < 5 || t > 19) {
            return 0.0;
        } else {
            double power = 0.0;
            for (int i = 0; i < solarCoefficients.size(); i++) {
                power += Math.abs(solarCoefficients.get(i) * Math.pow(t, i));
            }
            return power;
        }
    }
}
