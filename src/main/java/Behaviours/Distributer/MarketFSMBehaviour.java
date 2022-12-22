package Behaviours.Distributer;

import AdditionalClasses.JadePatternProvider;
import Behaviours.Distributer.FSMSubBehaviours.*;
import Behaviours.Distributer.FSMSubBehaviours.DivideEnergy.DivideEnergyBehaviour;
import Models.Consumer.ConsumerRequest;
import Models.Distributer.BestPriceContainer;
import Models.Distributer.DistributerMarketData;
import Models.Producer.ProducerPrice;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

import java.util.HashMap;
import java.util.Map;


public class MarketFSMBehaviour extends FSMBehaviour {
    private final String NOTIFY_PRODUCERS = "NOTIFY_PRODUCERS";
    private final String INIT = "INIT";
    private final String COLLECT_PRICES = "COLLECT_PRICES";
    private final String GET_THE_BEST = "GET_THE_BEST";
    private final String DIVIDE_ENERGY = "DIVIDE_ENERGY";
    private final String RESPOND_TO_CONSUMER = "RESPOND_TO_CONSUMER";
    private final String CONFIRM_DEAL = "CONFIRM_DEAL";
    private final String START_MARKET_AGAIN = "START_MARKET_AGAIN";
    private final String SEND_BAD_PRICE = "SEND_BAD_PRICE";
    public MarketFSMBehaviour(Agent a, DistributerMarketData marketData, long marketDuration) {
        AID topic = JadePatternProvider.connectToTopic(
                a, marketData.getMarketName()
        );
        long tradingDuration = marketDuration / 10;
        long confirmingDuration = tradingDuration / 10;
        ConsumerRequest requestMsg = marketData.getConsumerRequest();
        Map<AID, ProducerPrice> prices = new HashMap<>();
        BestPriceContainer bestPriceContainer = new BestPriceContainer();

        registerFirstState(new NotifyProducersBehaviour(marketData), NOTIFY_PRODUCERS);
        registerState(new InitiateMarketBehaviour(requestMsg, topic), INIT);
        registerState(new CollectPricesParallelBehaviour(tradingDuration, prices, topic), COLLECT_PRICES);
        registerState(new GetBestPriceBehaviour(
                prices, bestPriceContainer, topic, marketData.getConsumerRequest().getMaxPrice()),
                GET_THE_BEST);
        registerState(new ConfirmDealParallelBehaviour(getAgent(), bestPriceContainer, confirmingDuration), CONFIRM_DEAL);
        registerLastState(new DivideEnergyBehaviour(a, marketData, marketDuration), DIVIDE_ENERGY);
        registerLastState(new StartMarketAgainBehaviour(marketData, marketDuration), START_MARKET_AGAIN);
        registerLastState(new ResponseToConsumerBehaviour(bestPriceContainer, marketData.getConsumer()), RESPOND_TO_CONSUMER);
        registerLastState(new SendBadPriceBehaviour(marketData.getConsumer()), SEND_BAD_PRICE);

        registerDefaultTransition(NOTIFY_PRODUCERS, INIT);
        registerDefaultTransition(INIT, COLLECT_PRICES);
        registerDefaultTransition(COLLECT_PRICES, GET_THE_BEST);
        registerTransition(GET_THE_BEST, DIVIDE_ENERGY, 0);
        registerTransition(GET_THE_BEST, CONFIRM_DEAL, 1);
        registerTransition(GET_THE_BEST, SEND_BAD_PRICE, 2);
        registerTransition(CONFIRM_DEAL, START_MARKET_AGAIN, 0);
        registerTransition(CONFIRM_DEAL, RESPOND_TO_CONSUMER, 1);
    }
}
