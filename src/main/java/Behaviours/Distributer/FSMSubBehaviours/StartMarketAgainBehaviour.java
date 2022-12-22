package Behaviours.Distributer.FSMSubBehaviours;

import Behaviours.Distributer.DurationLimitedMarkedBehaviour;
import Models.Distributer.DistributerMarketData;
import jade.core.behaviours.OneShotBehaviour;

public class StartMarketAgainBehaviour extends OneShotBehaviour {
    private DistributerMarketData marketData;
    private long marketDuration;
    public StartMarketAgainBehaviour(DistributerMarketData marketData, long marketDuration) {
        this.marketData = marketData;
        this.marketDuration = marketDuration;
    }

    @Override
    public void action() {
        marketData.setMarketName(marketData.getMarketName() + " Repeated ");
        getAgent().addBehaviour(new DurationLimitedMarkedBehaviour(getAgent(), marketData, marketDuration));
    }
}
