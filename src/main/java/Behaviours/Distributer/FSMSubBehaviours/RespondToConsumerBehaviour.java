package Behaviours.Distributer.FSMSubBehaviours;

import Models.BestPriceContainer;
import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RespondToConsumerBehaviour extends OneShotBehaviour {
    private BestPriceContainer bestPriceContainer;
    public RespondToConsumerBehaviour(BestPriceContainer bestPriceContainer) {
        this.bestPriceContainer = bestPriceContainer;
    }

    @Override
    public void action() {

    }
}
