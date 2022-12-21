package Behaviours.Distributer.FSMSubBehaviours;

import AdditionalClasses.ParsingProvider;
import Models.ConsumerRequest;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitiateMarketBehaviour extends OneShotBehaviour {
    private ConsumerRequest requestMsg;
    private AID topic;
    public InitiateMarketBehaviour(ConsumerRequest requestMsg, AID topic) {
        this.requestMsg = requestMsg;
        this.topic = topic;
    }

    @Override
    public void action() {
        log.debug("Sends {} to topic", requestMsg);
        ACLMessage initRequest = new ACLMessage(ACLMessage.REQUEST);
        initRequest.setProtocol("InitMarket");
        initRequest.setContent(ParsingProvider.toJson(requestMsg));
        initRequest.addReceiver(topic);
        getAgent().send(initRequest);
    }
}