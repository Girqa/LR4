import AdditionalClasses.JadePatternProvider;
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
        Behaviour[] consumer = createConsumerBehaviours(t->10.0, 0.5, "Distributer", results);

        // Distributer behaviours
        List<ConsumerRequest> requests = new ArrayList<>();
        Behaviour getConsumerRequest = new GetConsumerRequestBehaviour(requests);
        // Producer behaviours
        Behaviour[] producer1 = createProducerBehaviours(t -> 10.0);
        Behaviour[] producer2 = createProducerBehaviours(t -> 5.0);
        Behaviour[] producer3 = createProducerBehaviours(t -> 0.0);

        kit.createAgent("Consumer", consumer);
        kit.createAgent("Distributer", getConsumerRequest);
        kit.createAgent("Producer1", producer1);
        kit.createAgent("Producer2", producer2);
        kit.createAgent("Producer3", producer3);

        while (timer.getMillisToNextHour() > timer.getMillisPerHour()/2){}

        assertEquals(0, results.size());
        assertEquals(1, requests.size());
    }

    private Behaviour[] createConsumerBehaviours(Function<Integer, Double> consumption,
                                                 double maxPrice,
                                                 String distributer,
                                                 List<BestPriceContainer> results){
        Behaviour sendOffer = new SendOfferBehaviour(
                consumption, maxPrice, new AID(distributer, false)
        );
        Behaviour getOrderResult = new GetOrderResultBehaviour(results);
        return new Behaviour[]{sendOffer, getOrderResult};
    }

    private Behaviour[] createProducerBehaviours(Function<Integer, Double> generation) {
        ProducerData data = new ProducerData(generation);
        timer.addListener(data);
        Behaviour registerYellowPage = new OneShotBehaviour() {
            @Override
            public void action() {
                JadePatternProvider.registerYellowPage(getAgent(), "Producer");
            }
        };
        Behaviour joinMarket = new JoinMarketBehaviour(data);
        Behaviour confirmDeal = new OneShotBehaviour() {
            @Override
            public void action() {
                getAgent().addBehaviour(new ConfirmDealWithDistributorBehaviour(getAgent(), data));
            }
        };
        Behaviour showCurrentPower = new OneShotBehaviour() {
            @Override
            public void action() {
                getAgent().addBehaviour(new ShowCurrentPowerBehaviour(getAgent(), data));
            }
        };
        return new Behaviour[]{registerYellowPage, joinMarket, confirmDeal, showCurrentPower};
    }
}
