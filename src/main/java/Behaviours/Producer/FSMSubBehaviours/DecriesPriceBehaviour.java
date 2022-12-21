package Behaviours.Producer.FSMSubBehaviours;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.ParsingProvider;
import Models.ProducerMarketData;
import Models.ProducerPrice;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import static jade.lang.acl.MessageTemplate.*;

@Slf4j
public class DecriesPriceBehaviour extends Behaviour {
    private ProducerMarketData marketData;
    private double finalPrice;
    private double step;
    private double lastPrice;
    private AID topic;
    private MessageTemplate mt;
    private boolean done;
    private int onEndResult;

    public DecriesPriceBehaviour(ProducerMarketData marketData) {
        this.marketData = marketData;
        this.lastPrice = marketData.getCurrentStartPrice();
        this.topic = marketData.getMarketTopic();
        this.done = false;
    }

    @Override
    public void onStart() {
        mt = and(
                and(
                        MatchPerformative(ACLMessage.INFORM),
                        MatchProtocol("Price")
                ),
                and(
                        MatchTopic(topic),
                        not(MatchSender(getAgent().getAID()))
                )
        );
    }

    @Override
    public void action() {
        ACLMessage agentsPrice = getAgent().receive(mt);
        if (agentsPrice != null && marketData.haveEnoughPower()) {
            ProducerPrice receivedPrice = ParsingProvider.fromJson(agentsPrice.getContent(), ProducerPrice.class);
            finalPrice = marketData.getCurrentFinalPrice();
            step = finalPrice / 5;
            double otherPrice = receivedPrice.getPrice();
            if (finalPrice <= otherPrice) {
                if (otherPrice - finalPrice > step && lastPrice >= otherPrice) {
                    ProducerPrice newPrice = new ProducerPrice(otherPrice - step, receivedPrice.getVolume());
                    lastPrice = newPrice.getPrice();
                    sendPrice(newPrice);
                } else if (lastPrice >= otherPrice){
                    ProducerPrice newPrice = new ProducerPrice(finalPrice, receivedPrice.getVolume());
                    lastPrice = newPrice.getPrice();
                    sendPrice(newPrice);
                    onEndResult = 1;
                    done = true;
                    JadePatternProvider.disconnectFromTopic(getAgent(), topic);
                }
            } else {
                //log.info("{} got final price {} from topic: {}", getAgent().getLocalName(), finalPrice, );
                onEndResult = 1;
                done = true;
                JadePatternProvider.disconnectFromTopic(getAgent(), topic);
            }
        } else if (!marketData.haveEnoughPower()) {
            onEndResult = 0;
            done = true;
            JadePatternProvider.disconnectFromTopic(getAgent(), topic);
        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        return onEndResult;
    }

    @Override
    public boolean done() {
        return done;
    }

    private void sendPrice(ProducerPrice price) {
        String content = ParsingProvider.toJson(price);
        log.debug("{} sends to {} data {}", getAgent().getLocalName(), topic.getLocalName(), content);
        ACLMessage newPriceMsg = new ACLMessage(ACLMessage.INFORM);
        newPriceMsg.setProtocol("Price");
        newPriceMsg.setContent(content);
        newPriceMsg.addReceiver(topic);
        getAgent().send(newPriceMsg);
    }
}
