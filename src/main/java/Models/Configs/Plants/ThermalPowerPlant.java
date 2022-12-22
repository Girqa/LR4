package Models.Configs.Plants;

import Interfaces.Generation;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ThermalPowerPlant")
public class ThermalPowerPlant implements Generation {
    @XmlElement(name = "power")
    private double power;
    @Override
    public Double apply(Integer t) {
        return power;
    }
}
