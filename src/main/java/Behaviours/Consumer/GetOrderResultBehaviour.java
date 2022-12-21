package Behaviours.Consumer;

import AdditionalClasses.ParsingProvider;
import Models.BestPriceContainer;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetOrderResultBehaviour extends Behaviour {
    private MessageTemplate mt;
    @Override
    public void onStart() {
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchProtocol("Trading result")
        );
    }

    @Override
    public void action() {
        ACLMessage result = getAgent().receive(mt);
        if (result != null) {
            BestPriceContainer container = ParsingProvider.fromJson(result.getContent(), BestPriceContainer.class);
            log.info("{} received order result {}", getAgent().getLocalName(), container);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
