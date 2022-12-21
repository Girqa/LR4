package Behaviours.Distributer;

import AdditionalClasses.Timer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BreakMarketOnTimeBehaviour extends Behaviour {
    private AID consumer;
    private Timer timer = Timer.getInstance();
    private long breakTime;
    private  boolean done;
    public BreakMarketOnTimeBehaviour(AID consumer) {
        this.consumer = consumer;
        breakTime = timer.getMillisPerHour() / 2;
    }

    @Override
    public void action() {
        if (breakTime > timer.getMillisToNextHour()) {
            log.info("{} finished Market because time is gone", getAgent().getLocalName());
            ACLMessage badResult = new ACLMessage(ACLMessage.INFORM);
            badResult.setProtocol("Trading result");
            badResult.setContent("Trading wasn't successful");
            getAgent().send(badResult);
            done = true;
        }
    }

    @Override
    public boolean done() {
        return done;
    }
}
