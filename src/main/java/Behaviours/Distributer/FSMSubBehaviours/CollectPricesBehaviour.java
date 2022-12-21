package Behaviours.Distributer.FSMSubBehaviours;

import AdditionalClasses.ParsingProvider;
import Models.ProducerPrice;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static jade.lang.acl.MessageTemplate.*;

@Slf4j
public class CollectPricesBehaviour extends Behaviour {
    private Map<AID, ProducerPrice> prices;
    private AID topic;
    private MessageTemplate mt;
    public CollectPricesBehaviour(Map<AID, ProducerPrice> prices, AID topic) {
        this.prices = prices;
        this.topic = topic;
    }

    @Override
    public void onStart() {
        mt = and(
                MatchTopic(topic),
                and(
                    MatchPerformative(ACLMessage.INFORM),
                    MatchProtocol("Price")
                )
        );
    }

    @Override
    public void action() {
        ACLMessage priceMsg = getAgent().receive(mt);
        if (priceMsg != null) {
            ProducerPrice price = ParsingProvider.fromJson(
                    priceMsg.getContent(), ProducerPrice.class
            );
            prices.put(priceMsg.getSender(), price);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
