package Models.Producer;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
@NoArgsConstructor
public class ProducerData implements Consumer<Integer> {
    private double currentPower;
    @JsonIgnore
    private Function<Integer, Double> generationExpression;
    @JsonIgnore
    private int lastHour;
    private double relativeEnergyCost;
    @JsonIgnore
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
