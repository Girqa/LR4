package Behaviours.Distributer.FSMSubBehaviours;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.PriceEntryComparator;
import Models.Distributer.BestPriceContainer;
import Models.Producer.ProducerPrice;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GetBestPriceBehaviour extends OneShotBehaviour {
    private Map<AID, ProducerPrice> prices;
    private BestPriceContainer bestPriceContainer;
    private AID topic;
    private boolean gotBestPrice;
    private double maxPrice;
    private int result;
    public GetBestPriceBehaviour(Map<AID, ProducerPrice> prices,
                                 BestPriceContainer bestPriceContainer,
                                 AID topic,
                                 double maxPrice) {
        this.prices = prices;
        this.bestPriceContainer = bestPriceContainer;
        this.topic = topic;
        this.maxPrice = maxPrice;
    }

    @Override
    public void action() {
        log.debug("{} getting best price", getAgent().getLocalName());
        Comparator<Map.Entry<AID, ProducerPrice>> comparator = new PriceEntryComparator();
        Optional<Map.Entry<AID, ProducerPrice>> minPriceEntry = prices.entrySet().stream().min(comparator);
        if (minPriceEntry.isPresent()) {
            if (minPriceEntry.get().getValue().getPrice() > maxPrice) {
                result = 2;
            } else {
                AID bestProducer = minPriceEntry.get().getKey();
                ProducerPrice bestPrice = minPriceEntry.get().getValue();
                log.debug("Best price is {} from {}", bestPrice, bestProducer.getLocalName());
                bestPriceContainer.setProducer(bestProducer);
                bestPriceContainer.setPrice(bestPrice);
                result = 1;
            }
        } else {
            log.debug("Can't choose best deal");
            result = 0;
        }
        JadePatternProvider.disconnectFromTopic(getAgent(), topic);
    }

    @Override
    public int onEnd() {
        return result;
    }
}
