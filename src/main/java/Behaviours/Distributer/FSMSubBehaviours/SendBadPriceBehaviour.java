package Behaviours.Distributer.FSMSubBehaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendBadPriceBehaviour extends OneShotBehaviour {
    private AID consumer;
    public SendBadPriceBehaviour(AID consumer) {
        this.consumer = consumer;
    }

    @Override
    public void action() {
        ACLMessage badPrice = new ACLMessage(ACLMessage.INFORM);
        badPrice.addReceiver(consumer);
        badPrice.setContent("There are no appropriate price");
        badPrice.setProtocol("Trading result");
        getAgent().send(badPrice);
    }
}
