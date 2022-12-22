package Behaviours.Distributer.FSMSubBehaviours.DivideEnergy;

import Behaviours.Distributer.DurationLimitedMarkedBehaviour;
import Models.Consumer.ConsumerRequest;
import Models.Distributer.DistributerMarketData;
import Models.Distributer.NetworkData;
import Models.Producer.ProducerData;
import jade.core.behaviours.OneShotBehaviour;

public class BalancedModeBehaviour extends OneShotBehaviour {
    private DistributerMarketData marketData;
    private NetworkData networkData;
    private long marketDuration;
    public BalancedModeBehaviour(DistributerMarketData marketData, NetworkData networkData, long marketDuration) {
        this.marketData = marketData;
        this.networkData = networkData;
        this.marketDuration = marketDuration;
    }

    @Override
    public void action() {
        double bestRelativeCost = networkData.getProducersData().get(0).getRelativeEnergyCost();
        ProducerData bestProducer = networkData.getProducersData().get(0);
        for (ProducerData producer: networkData.getProducersData()) {
            if (producer.getCurrentPower() != 0 && bestRelativeCost > producer.getRelativeEnergyCost()) {
                bestProducer = producer;
                bestRelativeCost = producer.getRelativeEnergyCost();
            }
        }
        double firstVolume = bestProducer.getCurrentPower();
        double secondVolume = marketData.getConsumerRequest().getEnergy() - firstVolume;
        ConsumerRequest previousRequest = marketData.getConsumerRequest();
        ConsumerRequest firstRequest = new ConsumerRequest(
                firstVolume,
                previousRequest.getMaxPrice() * firstVolume / previousRequest.getEnergy()
        );
        ConsumerRequest secondRequest = new ConsumerRequest(
                secondVolume,
                previousRequest.getMaxPrice() * secondVolume / previousRequest.getEnergy()
        );
        DistributerMarketData firstMarketData = new DistributerMarketData(
                marketData.getMarketName() + " f" + firstVolume,
                marketData.getConsumer(),
                firstRequest,
                1
        );
        DistributerMarketData secondMarketData = new DistributerMarketData(
                marketData.getMarketName() + " s" + secondVolume,
                marketData.getConsumer(),
                secondRequest,
                1
        );
        getAgent().addBehaviour(new DurationLimitedMarkedBehaviour(getAgent(), firstMarketData, marketDuration));
        getAgent().addBehaviour(new DurationLimitedMarkedBehaviour(getAgent(), secondMarketData, marketDuration));
    }
}
