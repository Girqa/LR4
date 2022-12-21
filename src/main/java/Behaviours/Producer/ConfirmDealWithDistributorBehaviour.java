package Behaviours.Producer;

import AdditionalClasses.ParsingProvider;
import Models.ProducerData;
import Models.ProducerPrice;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;
import lombok.extern.slf4j.Slf4j;

import static jade.lang.acl.MessageTemplate.*;

@Slf4j
public class ConfirmDealWithDistributorBehaviour extends AchieveREResponder {
    private ProducerData producerData;

    public ConfirmDealWithDistributorBehaviour(Agent a, ProducerData producerData) {
        super(a, and(
                MatchPerformative(ACLMessage.REQUEST),
                MatchProtocol("Confirm Deal")
        ));
        this.producerData = producerData;
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) {
        ACLMessage reply = request.createReply();
        ProducerPrice price = ParsingProvider.fromJson(request.getContent(), ProducerPrice.class);
        if (producerData.getCurrentPower() < price.getVolume()) {
            reply.setPerformative(ACLMessage.REFUSE);
            reply.setContent("Doesn't have enough power");
            log.info("{} can't confirm deal", getAgent().getLocalName());
        } else {
            reply.setPerformative(ACLMessage.AGREE);
            reply.setProtocol("Confirm deal");
            producerData.increaseBalance(price.getPrice());
            producerData.decreasePower(price.getVolume());
            reply.setContent(ParsingProvider.toJson(price));
            log.info("{} confirms deal", getAgent().getLocalName());
            log.debug("{} current balance: {}, current power: {}", getAgent().getLocalName(),
                    producerData.getBalance(), producerData.getCurrentPower());
        }
        return reply;
    }
}
