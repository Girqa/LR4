package Behaviours.Distributer.FSMSubBehaviours.DivideEnergy;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnbalancedModeBehaviour extends OneShotBehaviour {
    private AID consumer;
    public UnbalancedModeBehaviour(AID consumer) {
        this.consumer = consumer;
    }

    @Override
    public void action() {
        log.warn("Unbalanced mode!");
        ACLMessage badTrading = new ACLMessage(ACLMessage.INFORM);
        badTrading.setContent("Trading wasn't successful");
        badTrading.setProtocol("Trading result");
        badTrading.addReceiver(consumer);
        getAgent().send(badTrading);
    }
}
