package Models.Configs;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "consumer")
public class ConsumerCfg {
    public enum LoadType {
        HimProm, PishProm, Mpei
    }
    @XmlElement
    private String name;
    @XmlElement
    private String distributor;
    @XmlElement
    private LoadType loadType;
}
