package Agents;

import AdditionalClasses.ParsingProvider;
import Behaviours.Consumer.GetOrderResultBehaviour;
import Behaviours.Consumer.SendOfferBehaviour;
import Models.Configs.ConsumerCfg;
import Models.Configs.LoadConfig;
import jade.core.AID;
import jade.core.Agent;

import java.io.File;
import java.util.function.Function;

public class ConsumerAgent extends Agent {
    private String sourcePath = "./src/main/resources/";
    @Override
    protected void setup() {
        ConsumerCfg consumerCfg = ParsingProvider.unmarshal(
                new File(sourcePath+this.getLocalName()+".xml"),
                ConsumerCfg.class
        );
        LoadConfig loadConfig = getLoadConfig(consumerCfg.getLoadType());
        AID distributor = new AID(consumerCfg.getDistributor(), false);
        System.out.println(distributor);
        Function<Integer, Double> energyConsumption = loadConfig::getLoadAtHour;
        double maxPrice = 1.0;
        addBehaviour(new SendOfferBehaviour(energyConsumption, maxPrice, distributor));
        addBehaviour(new GetOrderResultBehaviour());
    }

    private LoadConfig getLoadConfig(ConsumerCfg.LoadType loadType) {
        return ParsingProvider.unmarshal(
                new File(sourcePath + "Loads/" + loadType + ".xml"), LoadConfig.class
        );
    }
}
