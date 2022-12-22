package Behaviours.Producer.FSMSubBehaviours;

import AdditionalClasses.ParsingProvider;
import Models.Producer.ProducerMarketData;
import Models.Producer.ProducerPrice;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendFirstPriceBehaviour extends OneShotBehaviour {
    private ProducerMarketData marketData;
    public SendFirstPriceBehaviour(ProducerMarketData data) {
        this.marketData = data;
    }

    @Override
    public void action() {
        double startPrice = marketData.getCurrentStartPrice();
        AID topic = marketData.getMarketTopic();
        log.info("{} joined {} with start price: {}",
                getAgent().getLocalName(), topic.getLocalName(), startPrice
        );
        ACLMessage priceMsg = new ACLMessage(ACLMessage.INFORM);
        priceMsg.setProtocol("Price");
        priceMsg.addReceiver(topic);
        ProducerPrice price = new ProducerPrice(startPrice, marketData.getConsumerNeeds());
        String content = ParsingProvider.toJson(price);
        priceMsg.setContent(content);
        getAgent().send(priceMsg);
    }
}
