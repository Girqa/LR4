package Behaviours.Distributer.FSMSubBehaviours.DivideEnergy;

import Models.Distributer.DistributerMarketData;
import Models.Distributer.NetworkData;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

public class DivideEnergyBehaviour extends FSMBehaviour {
    private final String GET_PRODUCERS_POWER = "GET_PRODUCERS_POWER";
    private final String CHECK_BALANCE = "CHECK_BALANCE";
    private final String BALANCED_MODE = "BALANCED_MODE";
    private final String UNBALANCED_MODE = "UNBALANCED_MODE";
    public DivideEnergyBehaviour(Agent a, DistributerMarketData marketData, long marketDuration) {
        NetworkData networkData = new NetworkData();
        long waitingTime = marketDuration / 100;

        registerFirstState(new GetProducersPowerParallelBehaviour(a, waitingTime, networkData), GET_PRODUCERS_POWER);
        registerState(new CheckBalanceBehaviour(marketData, networkData), CHECK_BALANCE);
        registerLastState(new UnbalancedModeBehaviour(marketData.getConsumer()), UNBALANCED_MODE);
        registerLastState(new BalancedModeBehaviour(marketData, networkData, marketDuration), BALANCED_MODE);

        registerDefaultTransition(GET_PRODUCERS_POWER, CHECK_BALANCE);
        registerTransition(CHECK_BALANCE,  UNBALANCED_MODE, 0);
        registerTransition(CHECK_BALANCE,  BALANCED_MODE, 1);
    }
}
