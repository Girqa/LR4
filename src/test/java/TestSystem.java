import AdditionalClasses.Timer;
import Behaviours.Consumer.GetOrderResultBehaviour;
import Behaviours.Consumer.SendOfferBehaviour;
import Behaviours.Distributer.GetConsumerRequestBehaviour;
import Behaviours.Producer.ConfirmDealWithDistributorBehaviour;
import Behaviours.Producer.JoinMarketBehaviour;
import Behaviours.Producer.ShowCurrentPowerBehaviour;
import JadeTestingClasses.JadeTestingKit;
import JadeTestingClasses.MockAgent;
import Models.Consumer.ConsumerRequest;
import Models.Distributer.BestPriceContainer;
import Models.Producer.ProducerData;
import com.sun.jdi.DoubleValue;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class TestSystem {
    private JadeTestingKit kit = new JadeTestingKit();
    private Timer timer = Timer.getInstance();
    @BeforeEach
    public void startJade() {
        kit.startJade();
    }

    @Test
    @SneakyThrows
    public void OneConsumerOneProducerCanSellButTooExpensive() {

        // Consumer behaviours
        List<BestPriceContainer> results = new ArrayList<>();
        Behaviour sendOffer = new SendOfferBehaviour(
                t -> 10.0, 0.5, new AID("Distributer", false)
        );
        Behaviour getOrderResult = new GetOrderResultBehaviour(results);
        // Distributer behaviours
        List<ConsumerRequest> requests = new ArrayList<>();
        MockAgent distributer = new MockAgent();
        Behaviour getConsumerRequest = new GetConsumerRequestBehaviour(requests);
        // Producer behaviours
        ProducerData data1 = new ProducerData(t ->5.0);
        timer.addListener(data1);
        Behaviour joinMarket = new JoinMarketBehaviour(data1);
        Behaviour confirmDeal = new OneShotBehaviour() {
            @Override
            public void action() {
                getAgent().addBehaviour(new ConfirmDealWithDistributorBehaviour(getAgent(), data1));
            }
        };
        Behaviour showCurrentPower = new OneShotBehaviour() {
            @Override
            public void action() {
                getAgent().addBehaviour(new ShowCurrentPowerBehaviour(getAgent(), data1));
            }
        };

        kit.createAgent("Consumer", sendOffer, getOrderResult);
        kit.createAgent("Distributer", getConsumerRequest);
        kit.createAgent("Producer", joinMarket, confirmDeal, showCurrentPower);

        while (timer.getMillisToNextHour() > timer.getMillisPerHour()/2){}

        assertEquals(0, results.size());
        assertEquals(1, requests.size());
    }
}
