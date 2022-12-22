package Behaviours.Distributer.FSMSubBehaviours.DivideEnergy;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.ParsingProvider;
import Models.Distributer.NetworkData;
import Models.Producer.ProducerData;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Vector;

@Slf4j
public class GetProducersPowerBehaviour extends AchieveREInitiator {
    private NetworkData networkData;
    public GetProducersPowerBehaviour(Agent a, NetworkData networkData) {
        super(a, new ACLMessage(ACLMessage.REQUEST));
        this.networkData = networkData;
    }

    @Override
    protected Vector prepareRequests(ACLMessage request) {
        log.debug("{} collects network data", getAgent().getLocalName());
        request.setProtocol("Available power");
        List<AID> producers = JadePatternProvider.getServiceProviders(getAgent(), "Producer");
        producers.forEach(request::addReceiver);
        return super.prepareRequests(request);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        ProducerData producerData = ParsingProvider.fromJson(inform.getContent(), ProducerData.class);
        log.debug("{} got {} from {}", getAgent().getLocalName(), producerData, inform.getSender());
        networkData.addProducerData(producerData);
    }
}
