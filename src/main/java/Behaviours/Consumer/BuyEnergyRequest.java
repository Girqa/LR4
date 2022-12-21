package Behaviours.Consumer;

import AdditionalClasses.ParsingProvider;
import AdditionalClasses.Timer;
import Models.ConsumerRequest;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class BuyEnergyRequest extends Behaviour {
    private int curHour;
    private Function<Integer, Double> energyConsumption;
    private AID distributer;
    private Timer timer;

    public BuyEnergyRequest(Function<Integer, Double> energyConsumption,
                            AID distributer) {
        this.timer = Timer.getInstance();
        this.curHour = timer.getCurHour() - 1;
        this.energyConsumption = energyConsumption;
        this.distributer = distributer;
    }

    @Override
    public void action() {
        int timerTime = timer.getCurHour();
        if (timerTime - curHour > 0) {
            curHour = timerTime;
            // логика отправки запроса
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.setProtocol("ConsumerRequest");
            String content = ParsingProvider.toJson(
                    new ConsumerRequest(energyConsumption.apply(curHour), curHour)
            );
            request.setContent(content);
            log.debug("{} sends {} to {}", getAgent().getLocalName(), content, distributer.getLocalName());
            request.addReceiver(distributer);

            getAgent().send(request);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
