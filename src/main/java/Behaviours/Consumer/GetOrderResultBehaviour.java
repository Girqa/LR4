package Behaviours.Consumer;

import AdditionalClasses.ParsingProvider;
import Models.Distributer.BestPriceContainer;
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
            if (result.getContent().equals("Trading wasn't successful")){
                log.warn(result.getContent());
            } else if (result.getContent().equals("There are no appropriate price")){
                log.warn(result.getContent());
            } else {
                BestPriceContainer container = ParsingProvider.fromJson(result.getContent(), BestPriceContainer.class);
                log.warn("{} received order result: {} from producer: {}",
                        getAgent().getLocalName(),
                        container.getPrice(),
                        container.getProducer().getLocalName()
                );
            }

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
