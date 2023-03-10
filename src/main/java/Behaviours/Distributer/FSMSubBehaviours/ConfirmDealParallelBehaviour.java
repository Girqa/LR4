package Behaviours.Distributer.FSMSubBehaviours;

import Models.Distributer.BestPriceContainer;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfirmDealParallelBehaviour extends ParallelBehaviour {
    private BestPriceContainer bestPriceContainer;
    public ConfirmDealParallelBehaviour(Agent a, BestPriceContainer bestPriceContainer, long confirmingDuration) {
        super(WHEN_ANY);
        this.bestPriceContainer = bestPriceContainer;
        addSubBehaviour(new ConfirmDealWithProducerBehaviour(a, bestPriceContainer));
        addSubBehaviour(new WakerBehaviour(a, confirmingDuration) {
            @Override
            protected void onWake() {
                if (!bestPriceContainer.isConfirmed()) {
                    log.debug("Confirming time gone {}", getAgent().getLocalName());
                }
            }
        });
    }

    @Override
    public int onEnd() {
        return bestPriceContainer.isConfirmed() ? 1: 0;
    }
}
