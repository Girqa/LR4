package Behaviours.Distributer.FSMSubBehaviours;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.ParsingProvider;
import Models.Distributer.DistributerMarketData;
import Models.Distributer.DistributerRequest;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class NotifyProducersBehaviour extends OneShotBehaviour {
    DistributerMarketData marketData;
    public NotifyProducersBehaviour(DistributerMarketData marketData) {
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
        log.info("{} connects producers to topic {}", getAgent().getLocalName(),  distributerRequest.getTopicName());
        getAgent().send(topicMsg);
    }
}
