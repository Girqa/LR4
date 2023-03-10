package Models.Distributer;

import Models.Producer.ProducerPrice;
import jade.core.AID;
import lombok.Data;

@Data
public class BestPriceContainer {
    private AID producer;
    private ProducerPrice price;
    private boolean confirmed;
}
