package Behaviours.Distributer.FSMSubBehaviours;

import AdditionalClasses.ParsingProvider;
import Models.BestPriceContainer;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RespondToConsumerBehaviour extends OneShotBehaviour {
    private BestPriceContainer bestPriceContainer;
    private AID consumer;
    public RespondToConsumerBehaviour(BestPriceContainer bestPriceContainer, AID consumer) {
        this.bestPriceContainer = bestPriceContainer;
        this.consumer = consumer;
    }

    @Override
    public void action() {
        ACLMessage response = new ACLMessage(ACLMessage.INFORM);
        response.addReceiver(consumer);
        response.setProtocol("Trading result");
        response.setContent(ParsingProvider.toJson(bestPriceContainer));

        getAgent().send(response);
    }
}
