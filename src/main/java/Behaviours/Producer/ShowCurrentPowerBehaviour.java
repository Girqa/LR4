package Behaviours.Producer;

import AdditionalClasses.ParsingProvider;
import Models.ProducerData;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;
import lombok.extern.slf4j.Slf4j;

import static jade.lang.acl.MessageTemplate.*;

@Slf4j
public class ShowCurrentPowerBehaviour extends AchieveREResponder {
    private ProducerData producerData;
    public ShowCurrentPowerBehaviour(Agent a, ProducerData producerData) {
        super(a, and(
                MatchPerformative(ACLMessage.REQUEST),
                MatchProtocol("Available power")
        ));
        this.producerData = producerData;
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) {
        ACLMessage reply = request.createReply();
        reply.setContent(ParsingProvider.toJson(producerData));
        reply.setPerformative(ACLMessage.INFORM);
        reply.setProtocol("Available power");
        log.debug("{} sends available power to {}", getAgent().getLocalName(), request.getSender().getLocalName());
        return reply;
    }
}
