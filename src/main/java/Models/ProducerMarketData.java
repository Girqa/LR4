package Models;

import jade.core.AID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс для хранения информации о текущей сделке продьюсера на рынке
 */
@Data
@AllArgsConstructor
public class ProducerMarketData {
    private double consumerNeeds;
    private ProducerData producerData;
    private AID marketTopic;

    public double getCurrentFinalPrice() {
        return consumerNeeds * producerData.getRelativeEnergyCost();
    }
    public double getCurrentStartPrice() {
        return getCurrentFinalPrice() * 2;
    }
    public boolean haveEnoughPower() {
        return producerData.getCurrentPower() >= consumerNeeds;
    }
}
