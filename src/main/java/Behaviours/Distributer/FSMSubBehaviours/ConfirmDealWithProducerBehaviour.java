package Behaviours.Distributer.FSMSubBehaviours;

import AdditionalClasses.ParsingProvider;
import Models.BestPriceContainer;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import lombok.extern.slf4j.Slf4j;

import java.util.Vector;

@Slf4j
public class ConfirmDealWithProducerBehaviour extends AchieveREInitiator {
    private BestPriceContainer bestPriceContainer;
    private boolean confirmed;
    public ConfirmDealWithProducerBehaviour(Agent a, BestPriceContainer bestPriceContainer) {
        super(a, new ACLMessage(ACLMessage.REQUEST));
        this.bestPriceContainer = bestPriceContainer;
    }

    @Override
    protected Vector prepareRequests(ACLMessage request) {
        log.debug("{} confirms deal with {}", getAgent().getLocalName(), bestPriceContainer.getProducer().getLocalName());
        request.addReceiver(bestPriceContainer.getProducer());
        request.setContent(ParsingProvider.toJson(bestPriceContainer.getPrice()));
        request.setProtocol("Confirm Deal");
        return super.prepareRequests(request);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        if (bestPriceContainer.getProducer().equals(agree.getSender())) {
            log.debug("{} got agree from {}", getAgent().getLocalName(), agree.getSender().getLocalName());
            confirmed = true;
        }
        super.handleAgree(agree);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        log.debug("{} got refuse from {}", getAgent().getLocalName(), refuse.getSender().getLocalName());
        confirmed = false;
        super.handleRefuse(refuse);
    }

    @Override
    public int onEnd() {
        if (confirmed) return 1;
        return 0;
    }
}
