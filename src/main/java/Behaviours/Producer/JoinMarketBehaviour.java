package Behaviours.Producer;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.ParsingProvider;
import Models.Distributer.DistributerRequest;
import Models.Producer.ProducerData;
import Models.Consumer.ConsumerRequest;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JoinMarketBehaviour extends Behaviour {
    private MessageTemplate mt;
    private ProducerData producerData;

    public JoinMarketBehaviour(ProducerData producerData) {
        this.producerData = producerData;
    }

    @Override
    public void onStart() {
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchProtocol("topic")
        );
    }

    @Override
    public void action() {
        ACLMessage request = getAgent().receive(mt);
        if (request != null) {
            log.debug("{} got market init msg {}", getAgent().getLocalName(), request.getContent());
            DistributerRequest distributerRequest = ParsingProvider.fromJson(
                    request.getContent(), DistributerRequest.class
            );
            ConsumerRequest consumerRequest = distributerRequest.getConsumerRequest();
            if (consumerRequest.getEnergy() <= producerData.getCurrentPower()) {
                log.warn("{} joined to market {}",
                        getAgent().getLocalName(), distributerRequest.getTopicName()
                );
                // Кароче, топик передавать JSON-ом нельзя...
                AID topic = JadePatternProvider.connectToTopic(getAgent(), distributerRequest.getTopicName());
                getAgent().addBehaviour(new TradeOnMarketFSMBehaviour(consumerRequest.getEnergy(), producerData, topic));
            } else {
                log.debug("{} doesn't have enough power: {}", getAgent().getLocalName(), producerData.getCurrentPower());
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
