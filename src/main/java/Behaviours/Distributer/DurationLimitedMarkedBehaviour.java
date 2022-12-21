package Behaviours.Distributer;

import Models.DistributerMarketData;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class DurationLimitedMarkedBehaviour extends ParallelBehaviour {
    public DurationLimitedMarkedBehaviour(Agent a, DistributerMarketData marketData, long marketDuration) {
        super(WHEN_ANY);
        if (marketData.getWasDivided() <= 1) {
            addSubBehaviour(new MarketFSMBehaviour(a, marketData, marketDuration));
            addSubBehaviour(new BreakMarketOnTimeBehaviour(marketData.getConsumer()));
        }
    }
}
