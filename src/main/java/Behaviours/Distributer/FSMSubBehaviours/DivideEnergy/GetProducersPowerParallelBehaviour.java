package Behaviours.Distributer.FSMSubBehaviours.DivideEnergy;

import Models.Distributer.NetworkData;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;

public class GetProducersPowerParallelBehaviour extends ParallelBehaviour {
    public GetProducersPowerParallelBehaviour(Agent a, long waitingTime, NetworkData networkData) {
        super(WHEN_ANY);
        addSubBehaviour(new GetProducersPowerBehaviour(a, networkData));
        addSubBehaviour(new WakerBehaviour(a, waitingTime) {
            @Override
            protected void onWake() {
            }
        });
    }
}
