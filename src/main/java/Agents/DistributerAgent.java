package Agents;

import Behaviours.Distributer.GetConsumerRequest;
import jade.core.Agent;

public class DistributerAgent extends Agent {
    @Override
    protected void setup() {
        // Чтение конфига
        String name = "Distributer";
        // Регистрация в df services
        ////
        // Добавление поведения получения запроса от потребителя
        addBehaviour(new GetConsumerRequest());
    }
}
