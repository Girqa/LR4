package Behaviours.Distributer;

import AdditionalClasses.ParsingProvider;
import AdditionalClasses.Timer;
import Models.Consumer.ConsumerRequest;
import Models.Distributer.DistributerMarketData;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetConsumerRequestBehaviour extends Behaviour {
    private MessageTemplate mt;
    private final Timer timer = Timer.getInstance();

    @Override
    public void onStart() {
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchProtocol("ConsumerRequest")
        );
    }

    @Override
    public void action() {
        ACLMessage request = getAgent().receive(mt);
        if (request != null) {
            ConsumerRequest consumerRequest = ParsingProvider.fromJson(
                    request.getContent(), ConsumerRequest.class
                    );
            log.warn("Got {} from Consumer", consumerRequest);
            DistributerMarketData data = new DistributerMarketData(
                    "Market: " + getAgent().getLocalName() + " at time: " +
                            timer.getCurHour() + "h " + timer.getMillisToNextHour() + "m",
                    request.getSender(),
                    consumerRequest,
                    0
            );
            getAgent().addBehaviour(new WakerBehaviour(getAgent(), 10L) {
                @Override
                protected void onWake() {
                    getAgent().addBehaviour(new DurationLimitedMarkedBehaviour(getAgent(), data, timer.getMillisToNextHour()));
                }
            });
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
