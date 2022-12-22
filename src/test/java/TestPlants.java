import AdditionalClasses.ParsingProvider;
import Models.Configs.Plants.SolarPowerPlant;
import Models.Configs.Plants.WindPowerPlant;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlants {
    @Test
    public void TestWindPowerPlantWithoutConfig() {
        WindPowerPlant p = new WindPowerPlant();
        p.setDispersion(10);
        p.setMeanValue(5);
        for (int i = 0; i < 100; i++) {
            assertFalse(p.apply(i) <= 0);
        }
    }

    @Test
    public void TestWindPowerPlantWithConfig() {
        WindPowerPlant p = ParsingProvider.unmarshal(
                new File("./src/main/resources/Plants/WindPowerPlant.xml"),
                WindPowerPlant.class
        );
        assertEquals(8.1, p.getMeanValue());
        assertEquals(9, p.getDispersion());
        for (int i = 0; i < 100; i++) {
            assertFalse(p.apply(0) <= 0);
        }
    }

    @Test
    public void TestSolarPowerPlantWithConfig() {
        SolarPowerPlant p = ParsingProvider.unmarshal(
                new File("./src/main/resources/Plants/SolarPowerPlant.xml"),
                SolarPowerPlant.class
        );
        List<Double> configCoefficients = List.of(-81.355, 20.922, -1.358, 0.025);
        assertIterableEquals(configCoefficients, p.getSolarCoefficients());
        for (int i = 0; i < 23; i++) {
            assertFalse(p.apply(i) < 0);
        }
    }
}
