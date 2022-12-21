package Behaviours.Distributer.FSMSubBehaviours;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.ParsingProvider;
import Models.DistributerMarketData;
import Models.DistributerRequest;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

public class NotifyProducers extends OneShotBehaviour {
    DistributerMarketData marketData;
    public NotifyProducers(DistributerMarketData marketData) {
        this.marketData = marketData;
    }

    @Override
    public void action() {
        List<AID> producers = JadePatternProvider.getServiceProviders(getAgent(), "Producer");
        DistributerRequest distributerRequest = new DistributerRequest(marketData.getConsumerRequest(), marketData.getMarketName());
        ACLMessage topicMsg = new ACLMessage(ACLMessage.INFORM);
        topicMsg.setContent(ParsingProvider.toJson(distributerRequest));
        topicMsg.setProtocol("topic");
        producers.forEach(topicMsg::addReceiver);
        getAgent().send(topicMsg);
    }
}
