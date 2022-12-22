package Models.Distributer;

import Models.Producer.ProducerData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NetworkData {
    private List<ProducerData> producersData;
    private double networkPower;
    public NetworkData() {
        producersData = new ArrayList<>();
        networkPower = 0.0;
    }
    public void addProducerData(ProducerData producerData) {
        networkPower += producerData.getCurrentPower();
        producersData.add(producerData);
    }
}
