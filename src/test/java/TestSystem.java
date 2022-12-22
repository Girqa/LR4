import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.Timer;
import Behaviours.Consumer.GetOrderResultBehaviour;
import Behaviours.Consumer.SendOfferBehaviour;
import Behaviours.Distributer.GetConsumerRequestBehaviour;
import Behaviours.Producer.ConfirmDealWithDistributorBehaviour;
import Behaviours.Producer.JoinMarketBehaviour;
import Behaviours.Producer.ShowCurrentPowerBehaviour;
import JadeTestingClasses.JadeTestingKit;
import Models.Consumer.ConsumerRequest;
import Models.Distributer.BestPriceContainer;
import Models.Producer.ProducerData;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
public class TestSystem {
    private JadeTestingKit kit = new JadeTestingKit();
    private Timer timer = Timer.getInstance();
    @BeforeEach
    public void startJade() {
        kit.startJade();
    }

    @AfterEach
    public void kilAgents() {
        kit.killAgents();
    }

    @Test
    public void OneConsumerOneProducerCanSellButTooExpensive() {
        timer.reInitiate(900);
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

        waitMaxTradeDuration();

        assertEquals(0, results.size());
        assertEquals(1, requests.size());
    }

    @Test
    public void OneConsumerTwoProducersCanSellOneWinner() {
        timer.reInitiate(900);
        // Consumer behaviours
        List<BestPriceContainer> results = new ArrayList<>();
        Behaviour[] consumer = createConsumerBehaviours(t->10.0, 0.5, "Distributer", results);

        // Distributer behaviours
        List<ConsumerRequest> requests = new ArrayList<>();
        Behaviour getConsumerRequest = new GetConsumerRequestBehaviour(requests);
        // Producer behaviours
        Behaviour[] producer1 = createProducerBehaviours(t -> 100.0);
        Behaviour[] producer2 = createProducerBehaviours(t -> 50.0);
        Behaviour[] producer3 = createProducerBehaviours(t -> 15.0);

        kit.createAgent("Consumer", consumer);
        kit.createAgent("Distributer", getConsumerRequest);
        kit.createAgent("Producer1", producer1);
        kit.createAgent("Producer2", producer2);
        kit.createAgent("Producer3", producer3);

        waitMaxTradeDuration();

        assertEquals(1, results.size());
        assertEquals(1, requests.size());
    }

    @Test
    public void OneConsumerDistributerDividesRequest() {
        timer.reInitiate(900);
        // Consumer behaviours
        List<BestPriceContainer> results = new ArrayList<>();
        double requestVolume = 40.0;
        double maxPrice = 4.0;
        Behaviour[] consumer = createConsumerBehaviours(t -> requestVolume, maxPrice, "Distributer", results);

        // Distributer behaviours
        List<ConsumerRequest> requests = new ArrayList<>();
        Behaviour getConsumerRequest = new GetConsumerRequestBehaviour(requests);
        // Producer behaviours
        Behaviour[] producer1 = createProducerBehaviours(t -> 20.0);
        Behaviour[] producer2 = createProducerBehaviours(t -> 20.0);
        Behaviour[] producer3 = createProducerBehaviours(t -> 15.0);

        kit.createAgent("Consumer", consumer);
        kit.createAgent("Distributer", getConsumerRequest);
        kit.createAgent("Producer1", producer1);
        kit.createAgent("Producer2", producer2);
        kit.createAgent("Producer3", producer3);

        waitMaxTradeDuration();
        log.info("Cur time");
        assertEquals(2, results.size());
        assertEquals(1, requests.size());
        assertEquals(requestVolume, results
                .stream()
                .map(bestPrice -> bestPrice.getPrice().getVolume())
                .reduce(Double::sum)
                .get()
        );
        assertTrue(results
                .stream()
                .map(bestPrice -> bestPrice.getPrice().getPrice())
                .reduce(Double::sum).get() <= maxPrice
        );
    }

    public void waitMaxTradeDuration() {
        while (timer.getMillisToNextHour() > timer.getMillisPerHour()/3){}
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
