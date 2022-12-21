package Behaviours.Distributer.FSMSubBehaviours;

import Models.ProducerPrice;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Map;

import static jade.lang.acl.MessageTemplate.*;

public class CleanPricesBehaviour extends Behaviour {
    private Map<AID, ProducerPrice> prices;
    private AID topic;
    private MessageTemplate mt;
    public CleanPricesBehaviour(Map<AID, ProducerPrice> prices, AID topic) {
        this.prices = prices;
        this.topic = topic;
    }

    @Override
    public void onStart() {
        mt = and(
                MatchPerformative(ACLMessage.REFUSE),
                and(
                        MatchProtocol("Refuse trading"),
                        MatchTopic(topic)
                )
        );
    }

    @Override
    public void action() {
        ACLMessage refuseTradingMsg = getAgent().receive(mt);
        if (refuseTradingMsg != null) {
            prices.remove(refuseTradingMsg.getSender());
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
