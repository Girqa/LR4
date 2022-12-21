package Behaviours.Distributer.FSMSubBehaviours;

import Models.ProducerPrice;
import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class CollectPricesParallelBehaviour extends ParallelBehaviour {
    public CollectPricesParallelBehaviour(long tradingDuration,
                                          Map<AID, ProducerPrice> prices,
                                          AID topic) {
        super(WHEN_ANY);
        addSubBehaviour(new WakerBehaviour(getAgent(), tradingDuration) {
            @Override
            protected void onWake() {
                log.debug("{} checks agents", getAgent().getLocalName());
            }
        });

        addSubBehaviour(new CollectPricesBehaviour(prices, topic));
        addSubBehaviour(new CleanPricesBehaviour(prices, topic));
    }
}
