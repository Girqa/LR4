package Agents;

import AdditionalClasses.JadePatternProvider;
import Behaviours.Producer.ConfirmDealWithDistributor;
import Models.ProducerData;
import AdditionalClasses.Timer;
import Behaviours.Producer.JoinMarket;
import jade.core.Agent;

import java.util.function.Function;

public class ProducerAgent extends Agent {
    @Override
    protected void setup() {
        // Чтение конфига
        Function<Integer, Double> generation = h -> 20.0;
        ProducerData data = new ProducerData(generation);
        Timer.getInstance().addListener(data);
        // главным образом - функция какая
        JadePatternProvider.registerYellowPage(this, "Producer");
        addBehaviour(new JoinMarket(data));
        addBehaviour(new ConfirmDealWithDistributor(this, data));
    }
}
