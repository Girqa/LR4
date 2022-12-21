package Models;

import lombok.Data;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class ProducerData implements Consumer<Integer> {
    private volatile double currentPower;
    private Function<Integer, Double> generationExpression;
    private int lastHour;
    private volatile double relativeEnergyCost;
    private double balance;

    public ProducerData(Function<Integer, Double> generationExpression) {
        this.generationExpression = generationExpression;
        currentPower = 0.0;
        relativeEnergyCost = 0.0;
        lastHour = -1;
        balance = 0.0;
    }
    @Override
    public void accept(Integer hour) {
        if (lastHour != hour) {
            lastHour = hour;
            currentPower = generationExpression.apply(lastHour);
            if (currentPower != 0.0) {
                relativeEnergyCost = 1 / currentPower;
            } else {
                relativeEnergyCost = Double.MAX_VALUE;
            }
        }
    }

    public void increaseBalance(double money) {
        balance += money;
    }
    public void decreasePower(double power) {
        double newPower = getCurrentPower() - power;
        setCurrentPower(newPower);
    }
}
