package Behaviours.Distributer.FSMSubBehaviours.DivideEnergy;

import Models.DistributerMarketData;
import Models.NetworkData;
import jade.core.behaviours.OneShotBehaviour;

public class CheckBalanceBehaviour extends OneShotBehaviour {
    private DistributerMarketData marketData;
    private NetworkData networkData;
    private boolean isBalanced;
    public CheckBalanceBehaviour(DistributerMarketData marketData, NetworkData networkData) {
        this.marketData = marketData;
        this.networkData = networkData;
    }

    @Override
    public void action() {
        isBalanced = networkData.getNetworkPower() >= marketData.getConsumerRequest().getEnergy();
    }

    @Override
    public int onEnd() {
        return isBalanced ? 1: 0;
    }
}
