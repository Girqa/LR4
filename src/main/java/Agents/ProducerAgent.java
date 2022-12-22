package Agents;

import AdditionalClasses.JadePatternProvider;
import AdditionalClasses.ParsingProvider;
import Behaviours.Producer.ConfirmDealWithDistributorBehaviour;
import Behaviours.Producer.ShowCurrentPowerBehaviour;
import Interfaces.Generation;
import Models.Configs.Plants.SolarPowerPlant;
import Models.Configs.Plants.ThermalPowerPlant;
import Models.Configs.Plants.WindPowerPlant;
import Models.Producer.ProducerData;
import AdditionalClasses.Timer;
import Behaviours.Producer.JoinMarketBehaviour;
import jade.core.Agent;

import java.io.File;

public class ProducerAgent extends Agent {
    private String source = "./src/main/resources/";
    @Override
    protected void setup() {
        // Чтение конфига
        Generation generation = getPlant(this.getLocalName());
        ProducerData data = new ProducerData(generation);
        Timer.getInstance().addListener(data);
        // главным образом - функция какая
        JadePatternProvider.registerYellowPage(this, "Producer");
        addBehaviour(new JoinMarketBehaviour(data));
        addBehaviour(new ConfirmDealWithDistributorBehaviour(this, data));
        addBehaviour(new ShowCurrentPowerBehaviour(this, data));
    }

    private Generation getPlant(String localName) {
        switch (localName) {
            case "Wind":
                return ParsingProvider.unmarshal(
                        new File(source + "Plants/" + "WindPowerPlant.xml"),
                        WindPowerPlant.class
                );
            case "Thermal":
                return ParsingProvider.unmarshal(
                        new File(source + "Plants/" + "ThermalPowerPlant.xml"),
                        ThermalPowerPlant.class
                );
            case "Solar":
                return ParsingProvider.unmarshal(
                        new File(source + "Plants/" + "SolarPowerPlant.xml"),
                        SolarPowerPlant.class
                );
            default:
                throw new IllegalArgumentException("Incorrect agent name");
        }
    }
}
