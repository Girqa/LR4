package Behaviours.Producer;

import Behaviours.Producer.FSMSubBehaviours.DecriesPriceBehaviour;
import Behaviours.Producer.FSMSubBehaviours.RefuseTradingBehaviour;
import Behaviours.Producer.FSMSubBehaviours.RichesFinalPrice;
import Behaviours.Producer.FSMSubBehaviours.SendFirstPriceBehaviour;
import Models.MarketDealData;
import Models.ProducerData;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TradeOnMarketFSMBehaviour extends FSMBehaviour {
    private final String FIRST_PRICE = "FIRST_PRICE", DECRIES_PRICE = "DECRIES_PRICE", FINAL_PRICE = "FINAL_PRICE",
    NOT_ENOUGH_POWER = "NOT_ENOUGH_POWER";


    public TradeOnMarketFSMBehaviour(double consumerNeeds, ProducerData producerData, AID topic) {
        MarketDealData marketDealData = new MarketDealData(consumerNeeds, producerData, topic);

        registerFirstState(new SendFirstPriceBehaviour(marketDealData), FIRST_PRICE);
        registerState(new DecriesPriceBehaviour(marketDealData), DECRIES_PRICE);
        registerLastState(new RichesFinalPrice(), FINAL_PRICE);
        registerLastState(new RefuseTradingBehaviour(marketDealData.getMarketTopic()), NOT_ENOUGH_POWER);

        registerDefaultTransition(FIRST_PRICE, DECRIES_PRICE);
        registerTransition(DECRIES_PRICE, FINAL_PRICE, 1);
        registerTransition(DECRIES_PRICE, NOT_ENOUGH_POWER, 0);
    }
}
