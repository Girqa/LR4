package Agents;

import Behaviours.Consumer.GetOrderResultBehaviour;
import Behaviours.Consumer.SendOfferBehaviour;
import jade.core.AID;
import jade.core.Agent;

import java.util.function.Function;

public class ConsumerAgent extends Agent {
    @Override
    protected void setup() {
        // Получить своего DistibuterAgent из конфига
        String distibuter = "Distributer";
        AID dis = new AID(distibuter, false);
        // Получить свой график нагрузки
        Function<Integer, Double> energyConsumption = h -> 30.0/(h+1);
        // Поведение отправки запроса поставщику
        addBehaviour(new SendOfferBehaviour(energyConsumption, dis));
        addBehaviour(new GetOrderResultBehaviour());
    }
}
