package Behaviours.Distributer;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.Timer;
import Behaviours.Distributer.FSMSubBehaviours.*;
import Models.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

import java.util.HashMap;
import java.util.Map;


public class MarketFMSBehaviour extends FSMBehaviour {
    private final String NOTIFY_PRODUCERS = "NOTIFY_PRODUCERS";
    private final String INIT = "INIT";
    private final String COLLECT_PRICES = "COLLECT_PRICES";
    private final String GET_THE_BEST = "GET_THE_BEST";
    private final String DIVIDE_ENERGY = "DIVIDE_ENERGY";
    private final String RESPOND_TO_CONSUMER = "RESPOND_TO_CONSUMER";
    private final String CONFIRM_DEAL = "CONFIRM_DEAL";
    public MarketFMSBehaviour(Agent a, DistributerMarketData marketData) {
        AID topic = JadePatternProvider.connectToTopic(
                a, marketData.getMarketName()
        );
        long tradingDuration = Timer.getInstance().getMillisToNextHour() / 10;
        ConsumerRequest requestMsg = marketData.getConsumerRequest();
        Map<AID, ProducerPrice> prices = new HashMap<>();
        BestPriceContainer bestPriceContainer = new BestPriceContainer();

        registerFirstState(new NotifyProducers(marketData), NOTIFY_PRODUCERS);
        registerState(new InitiateMarketBehaviour(requestMsg, topic), INIT);
        registerState(new CollectPricesParallelBehaviour(tradingDuration, prices, topic), COLLECT_PRICES);
        registerState(new GetBestPriceBehaviour(prices, bestPriceContainer, topic), GET_THE_BEST);
        registerState(new ConfirmDealWithProducerBehaviour(getAgent(), bestPriceContainer), CONFIRM_DEAL);
        registerState(new DivideEnergyBehaviour(), DIVIDE_ENERGY);
        registerLastState(new RespondToConsumerBehaviour(bestPriceContainer), RESPOND_TO_CONSUMER);

        registerDefaultTransition(NOTIFY_PRODUCERS, INIT);
        registerDefaultTransition(INIT, COLLECT_PRICES);
        registerDefaultTransition(COLLECT_PRICES, GET_THE_BEST);
        registerTransition(GET_THE_BEST, DIVIDE_ENERGY, 0);
        registerTransition(GET_THE_BEST, CONFIRM_DEAL, 1);
        registerTransition(CONFIRM_DEAL, INIT, 0);
        registerTransition(CONFIRM_DEAL, RESPOND_TO_CONSUMER, 1);
        registerDefaultTransition(DIVIDE_ENERGY, RESPOND_TO_CONSUMER);
    }
}
