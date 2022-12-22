package Models.Configs.Plants;

import Interfaces.Generation;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Random;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "WindPowerPlant")
public class WindPowerPlant implements Generation {
    @XmlElement(name = "meanValue")
    private double meanValue;
    @XmlElement(name = "dispersion")
    private double dispersion;
    private Random random = new Random();

    @Override
    public Double apply(Integer t) {
        return Math.abs(random.nextGaussian())*dispersion + meanValue;
    }
}
