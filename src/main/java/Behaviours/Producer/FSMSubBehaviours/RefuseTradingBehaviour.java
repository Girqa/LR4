package Behaviours.Producer.FSMSubBehaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RefuseTradingBehaviour extends OneShotBehaviour {
    private AID topic;
    public RefuseTradingBehaviour(AID topic) {
        this.topic = topic;
    }

    @Override
    public void action() {
        log.info("{} refuses trading in {}", getAgent().getLocalName(), topic.getLocalName());
        ACLMessage refuse = new ACLMessage(ACLMessage.REFUSE);
        refuse.setProtocol("Refuse trading");
        refuse.setContent(getAgent().getLocalName());
        refuse.addReceiver(topic);
        getAgent().send(refuse);
    }
}
