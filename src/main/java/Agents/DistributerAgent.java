package Agents;

import Behaviours.Distributer.GetConsumerRequestBehaviour;
import jade.core.Agent;

public class DistributerAgent extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new GetConsumerRequestBehaviour());
    }
}
